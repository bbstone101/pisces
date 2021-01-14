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

@Slf4j
public class FileReqHandler implements CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileReq msg) {
        log.info("filepath: {}", msg.getFilepath());
        long reqTs = msg.getTs();
        String filepath = msg.getFilepath();

        if (Files.notExists(Paths.get(filepath))) {
            log.error("not found file in filepath: {}", filepath);
            return;
        }

        if (Files.isDirectory(Paths.get(filepath))) {
            sendDir(ctx, new File(filepath), reqTs);
        } else {
            sendFile(ctx, filepath, reqTs);
        }
    }

    private void sendDir(ChannelHandlerContext ctx, File file, long reqTs) {
        File[] flist = file.listFiles();
        Arrays.sort(flist);
        for (File subfile : flist) {
            if (subfile.isDirectory()) {
                sendDir(ctx, subfile, reqTs);
//                doSendDir(ctx, subfile.getAbsolutePath(), reqTs);
            }
            log.info("searching.... {} ......", subfile.getAbsolutePath());
            sendFile(ctx, subfile.getAbsolutePath(), reqTs);
        }
    }

    private void doSendDir(ChannelHandlerContext ctx, String filepath, long reqTs) {
        log.info("===|==|===|===|====dir: {}", filepath);
        if (Config.serverDir.equals(filepath)) {
            return;
        }
        /**
         * Standard Rsp format like:
         * +--------------------------------------------------------+
         * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info |
         * +--------------------------------------------------------+
         * <p>
         */
        ByteBuf rspBuf = BFileUtil.buildRspDir(filepath, reqTs);
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
     * @param filepath -  server file path
     * @param reqTs    - timestamp of client request this file
     */
    private void sendFile(ChannelHandlerContext ctx, String filepath, long reqTs) {
        log.info(">>>>>>>>>> sending file/dir: {}", filepath);
        if (Files.isDirectory(Paths.get(filepath))) {
            doSendDir(ctx, filepath, reqTs);
            return;
        }
        // file checksume
        String checksum = BFileUtil.checksum(filepath);

        // file content size of server file(which path is filepath)
        long filelen = new File(filepath).length();
        log.info("filelen: {}, write BFileRsp to client......", filelen);

        // ------------ send file chunk by chunk
        int pos = 0;
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
            ByteBuf rspBuf = BFileUtil.buildRspFile(filepath, filelen, checksum, reqTs);
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

}
