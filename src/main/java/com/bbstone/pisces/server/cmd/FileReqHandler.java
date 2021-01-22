package com.bbstone.pisces.server.cmd;

import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileReqHandler implements CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileReq msg) {
        log.debug("filepath: {}", msg.getFilepath());
        long reqTs = msg.getTs();
        // TODO filepath should be relative path of server.dir send by client,
        // so the first request from client should be CMD_LIST to get server.dir file list
        String filepath = msg.getFilepath();
        String serverpath = BFileUtil.getServerDir() + filepath;

        if (Files.notExists(Paths.get(serverpath))) {
            log.error("not found file in serverpath: {}", serverpath);
            return;
        }

        sendFile(ctx, serverpath, reqTs);
        log.debug("------> server done sent file: {}", serverpath);
/*
        List<String> fileList = BFileUtil.findFiles(serverpath);
        for (String file : fileList) {
            sendFile(ctx, file, reqTs);
            log.debug("------> server done sent file: {}", file);
        }

        if (Files.isDirectory(Paths.get(serverpath))) {
            sendDir(ctx, new File(serverpath), reqTs);
        } else {
            sendFile(ctx, serverpath, reqTs);
        }
        */
    }

    private void sendDir(ChannelHandlerContext ctx, File file, long reqTs) {
        File[] flist = file.listFiles();
        Arrays.sort(flist);
        for (File subfile : flist) {
            if (subfile.isDirectory()) {
                sendDir(ctx, subfile, reqTs);
//                doSendDir(ctx, subfile.getAbsolutePath(), reqTs);
            }
            log.debug("searching.... {} ......", subfile.getAbsolutePath());
            sendFile(ctx, subfile.getAbsolutePath(), reqTs);
        }
    }

    private void doSendDir(ChannelHandlerContext ctx, String serverpath, long reqTs) {
        log.debug("===|==|===|===|====dir: {}", serverpath);
        if (Config.serverDir.equals(serverpath)) {
            return;
        }
        /**
         * Standard Rsp format like:
         * +--------------------------------------------------------+
         * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info |
         * +--------------------------------------------------------+
         * <p>
         */
        ByteBuf rspBuf = BFileUtil.buildRspDir(serverpath, reqTs);
        ctx.write(rspBuf);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
    }

    /**
     *
     * Non-standard format(append byte[] data after Rsp object, because cannot retrieve the data and set to Rsp.chunkData:
     * e.g. FileRsp(cmd: CMD_REQ) data format(only for FileRegion which directly write file data to channel):
     * +---------------------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | chunk_data | delimiter |
     * +---------------------------------------------------------------------------------+
     * <p>
     *
     * @param ctx
     * @param serverpath -  server file full path
     * @param reqTs    - timestamp of client request this file
     */
    private void sendFile(ChannelHandlerContext ctx, String serverpath, long reqTs) {
        log.debug(">>>>>>>>>> sending file/dir: {}", serverpath);
        if (Files.isDirectory(Paths.get(serverpath))) {
            doSendDir(ctx, serverpath, reqTs);
            return;
        }
        // file checksume
        String checksum = BFileUtil.checksum(new File(serverpath));

        // file content size of server file(which path is filepath)
        long filelen = new File(serverpath).length();
        log.debug("filelen: {}, write BFileRsp to client......", filelen);

        long startTime = System.currentTimeMillis();
        // ------------ send file chunk by chunk
        long pos = 0;
        int chunkCounter = 0;
        long chunkSize = 0;
        while ((filelen - pos) > 0) {
            /**
             * Standard Rsp format like:
             * +--------------------------------------------------------+
             * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info |
             * +--------------------------------------------------------+
             * <p>
             */
            ByteBuf rspBuf = BFileUtil.buildRspFile(serverpath, filelen, checksum, reqTs);
            int rspInfoLen = rspBuf.readableBytes();
            ctx.write(rspBuf);
            /**
             * Non-standard format:
             * appending send following data to channel directly(not assemble to full data format because
             * FileRegion not support extract data (TBD)
             *
             * +------------------------+
             * | chunk_data | delimiter |
             * +------------------------+
             */
            chunkSize = Math.min(ConstUtil.DEFAULT_CHUNK_SIZE, (filelen - pos));
            log.debug("current pos: {}, will write {} bytes to channel.", pos, chunkSize);
            // DefaultFileRegion need to pass new File(filepath) other than raf.getChannel(),
            // because every time new DefaultFileRegion, open a new raf
            ctx.write(new DefaultFileRegion(new File(serverpath), pos, chunkSize));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
            log.debug("output file: {}", serverpath);
            chunkCounter++;
            pos += chunkSize;
            log.info("=============== wrote the {} chunk, wrote len: {}, progress: {}/{} =============", chunkCounter, (rspInfoLen+chunkSize), pos, filelen);

        }
        log.info("write file({}) to channel cost time: {} sec.", serverpath, (System.currentTimeMillis() - startTime)/1000);

    }

}
