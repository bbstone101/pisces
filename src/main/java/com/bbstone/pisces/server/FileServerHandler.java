/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.bbstone.pisces.server;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.proto.BFileBuilder;
import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.BFileCodecUtil;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.proto.model.BFileRequest;
import com.bbstone.pisces.proto.model.BFileResponse;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.ConstUtil;
import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * cannot used ObjectEncoder/ObjectDecoder, because FileRegion write bytes to socket channel directly,
 * and no api to set chunk to a object,
 *
 * the solution just send every chunk with BFileResponse prefix, and end with delimiter "__BBSTONE_BFILE_END__"
 *
 */
@Slf4j
public class FileServerHandler extends SimpleChannelInboundHandler<BFileMsg.BFileReq> {

    // file content size of server file(which path is filepath)
    long filelen = 0;

    int pos = 0;
    int chunkCounter = 0;

    long chunkSize = 0;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush("HELLO: Type the path of the file to retrieve.\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BFileMsg.BFileReq msg) throws Exception {
        log.info("filepath: {}", msg.getFilepath());

        long reqTs = msg.getTs();
        String filepath = msg.getFilepath();

        if (Files.isDirectory(Paths.get(filepath))) {
            listDir(ctx, filepath);
        } else {
            // send file
            String checksum = DigestUtils.md5DigestAsHex(new FileInputStream(filepath));
            // note: The return value is unspecified if this pathname denotes a directory.
            this.filelen = new File(filepath).length();

            log.info("write BFileRsp to client......");
            sendChunk(ctx, filepath, checksum, reqTs);
        }

    }

    private void listDir(ChannelHandlerContext ctx, String filepath) {
        String fileTree = BFileUtil.list(filepath);
        ctx.write(fileTree);
        log.debug("fileTree: {} {}, {}", BFileUtil.LF, filepath, fileTree);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
    }

    /**
     * data format:
     * +---------------------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | chunk_data | delimiter |
     * +---------------------------------------------------------------------------------+
     *
     * bfile_info_prefix:
     *
     * @param ctx
     * @param filepath -  server file path
     * @param checksum -  file checksume
     * @param reqTs - timestamp of client request this file
     */
    private void sendChunk(ChannelHandlerContext ctx, String filepath, String checksum, long reqTs) {
        while ((filelen - pos) > 0) {
            // BFile info prefix
            byte[] prefix = BByteUtil.toBytes(ConstUtil.bfile_info_prefix);
            // BFile info
            BFileMsg.BFileRsp rsp = BFileMsg.BFileRsp.newBuilder()
                    .setMagic(ConstUtil.magic)
                    .setCmd(BFileCmd.CMD_RSP)
                    .setFilepath(filepath)
                    .setFileSize(filelen)
                    .setChecksum(checksum)
//                    .setFileChunkData(ByteString.copyFrom("Hello".getBytes(CharsetUtil.UTF_8)))
                    .setReqTs(reqTs)
                    .setRspTs(System.currentTimeMillis())
                    .build();
            byte[] rspWithoutFileData = rsp.toByteArray();

            int bfileInfoLen = rspWithoutFileData.length;
            byte[] bifileInfoBytes = BByteUtil.toBytes(bfileInfoLen);

            /**
             * bytes size format:
             * +----------------------------------------------------------------+
             * | bfile_info_prefix_len | bfile_info_bytes(int) | bfile_info_len |
             * +----------------------------------------------------------------+
             *
             */
            // assemble data
            int cpos = 0;
            byte[] data = new byte[prefix.length + Integer.BYTES + rspWithoutFileData.length];
            // bfile_info_prefix
            System.arraycopy(prefix, 0, data, cpos, prefix.length);
            // bfile_info size
            cpos += prefix.length;
            System.arraycopy(bifileInfoBytes, 0, data, cpos, Integer.BYTES);
            // bfile_info
            cpos += Integer.BYTES;
            System.arraycopy(rspWithoutFileData, 0, data, cpos, rspWithoutFileData.length);

            log.info("data.len: {}, bytes: {}", data.length, data.toString());
            ctx.write(Unpooled.wrappedBuffer(data));


            /**
             * appending send following data to channel directly(not assemble to full data format because
             * FileRegion not support extract data (TBD)
             *
             * +-------------------------------------
             * | chunk_data | delimiter |
             * +-------------------------------------
             */
            chunkSize = Math.min(ConstUtil.DEFAULT_CHUNK_SIZE, (filelen - pos));
            log.info("current pos: {}, will write {} bytes to channel.", pos, chunkSize);
            // DefaultFileRegion need to pass new File(filepath) other than raf.getChannel(),
            // because every time new DefaultFileRegion, open a new raf
            ctx.write(new DefaultFileRegion(new File(filepath), pos, chunkSize));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));

            chunkCounter++;
            pos += chunkSize;
            log.info("=============== wrote the {} chunk, wrote len: {}, progress: {}/{} =============", chunkCounter, chunkSize, pos, filelen);

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();

        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: " +
                    cause.getClass().getSimpleName() + ": " +
                    cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
    }


    private void writeChunk(ChannelHandlerContext ctx, RandomAccessFile raf, int pos, int length) {
        ctx.write(new DefaultFileRegion(raf.getChannel(), pos, length));
        ctx.writeAndFlush(ConstUtil.delimiter);
    }
}

