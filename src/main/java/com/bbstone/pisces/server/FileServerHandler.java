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
import com.bbstone.pisces.proto.BFileCodecUtil;
import com.bbstone.pisces.proto.model.BFileAck;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.proto.model.BFileRequest;
import com.bbstone.pisces.proto.model.BFileResponse;
import com.bbstone.pisces.util.ConstUtil;
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

/**
 * cannot used ObjectEncoder/ObjectDecoder, because FileRegion write bytes to socket channel directly,
 * and no api to set chunk to a object,
 *
 * the solution just send every chunk with BFileResponse prefix, and end with delimiter "__BBSTONE_BFILE_END__"
 *
 */
@Slf4j
public class FileServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    int pos = 0;
    int chunkCounter = 0;
    long filelen = 0;

    String filepath = null;
    String checkSum = null;

    long chunkSize = 0;

    int failCount = 0;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush("HELLO: Type the path of the file to retrieve.\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        BFileBase bFileBase = BFileCodecUtil.decode(msg);
        if (bFileBase instanceof BFileRequest) {
            BFileRequest bFileRequest = (BFileRequest) bFileBase;
            // initial and send the first chunk
            this.filepath = bFileRequest.getFilepath();
            this.checkSum = DigestUtils.md5DigestAsHex(new FileInputStream(filepath));
            // note: The return value is unspecified if this pathname denotes a directory.
            this.filelen = new File(filepath).length();
            //
            long startTime = System.currentTimeMillis();
            log.info("********** startTime: {} ", startTime);
            sendChunk(ctx);

        } /*else if (bFileBase instanceof BFileAck) {
            BFileAck bFileAck = (BFileAck) bFileBase;
            if (bFileAck.getAck() == ConstUtil.ACK_OK || failCount >= 3) {
                failCount = 0;
                log.info("send next chunk.....");
                pos += chunkSize;
                chunkCounter++;
                // send next chunk
                sendChunk(ctx);
            } else {
                failCount ++;
                log.warn(" last chunk ack fail, try to send it again....");
                // send previous chunk again(pos not increase)
                sendChunk(ctx);
            }

        }*/
    }

    private void sendChunk(ChannelHandlerContext ctx) {
        while ((filelen - pos) > 0) {
            chunkSize = Math.min(ConstUtil.DEFAULT_CHUNK_SIZE, (filelen - pos));
            log.info("current pos: {}, will write {} bytes to channel.", pos, chunkSize);

            BFileResponse bfile = BFileBuilder.buildRsp(filepath, checkSum, filelen, chunkSize);
            log.info("server bfile: {}", JSON.toJSONString(bfile));
            ByteBuf cbuf = BFileCodecUtil.encode(bfile);
            ctx.write(cbuf);

            // DefaultFileRegion need to pass new File(filepath) other than raf.getChannel(),
            // because every time new DefaultFileRegion, open a new raf
            ctx.write(new DefaultFileRegion(new File(filepath), pos, chunkSize));

            chunkCounter++;
            pos += chunkSize;

            log.info("=============== wrote the {} chunk, wrote len: {}, progress: {}/{} =============", chunkCounter, chunkSize, pos, filelen);
            ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
        } /*else {
            log.info("all chunks sent.");
        }*/

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

