package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class FileRspHandler implements CmdHandler {

    long recvSize = 0;
    int chunkCounter = 0;

    FileOutputStream fos = null;
    String clipath = null;
    String temppath = null;
    File tempfile = null;


    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        byte[] chunkData = null;
        // decode chunk data and set to rsp
        int chunkSize = msg.readableBytes();
        if (chunkSize == 0) {
            log.error("chunk data is 0.");
        }
        chunkData = new byte[chunkSize];
        msg.readBytes(chunkData);
        // not work set chunkData to rsp
        //rsp.toBuilder().setFileChunkData(ByteString.copyFrom(chunkData));
        // ---------------
        int chunkLen = chunkData.length;

        try {
            String filepath = rsp.getFilepath();
            clipath = BFileUtil.getCliFilepath(filepath);
            temppath = BFileUtil.getCliTempFilepath(clipath);
            log.info("*********clipath: {}, temppath: {}*********", clipath, temppath);
            tempfile = new File(temppath);

            FileOutputStream fos = new FileOutputStream(tempfile, true);
            fos.write(chunkData);
            fos.close();
            log.info("wrote current chunk to disk done.");

            chunkCounter++;
            recvSize += chunkLen;
            log.info("recv chunkLen: {}, progress: {}/{}", chunkLen, recvSize, rsp.getFileSize());
            log.info("============ chunk recv done==========");

            // all file data received
            if (recvSize > 0 && rsp.getFileSize() == recvSize) {
                log.info("all bytes received, try to close fos.");
                recvSize = 0; // reset counter

                // check file integrity
                String checkSum = BFileUtil.checksum(temppath);
                log.info("server checksum: {}, client checksum: {}, isEq: {}", rsp.getChecksum(), checkSum, (rsp.getChecksum().equals(checkSum)));

                if (rsp.getChecksum().equals(checkSum)) {
                    BFileUtil.renameCliTempFile(tempfile, clipath);
                }
                long endTime = System.currentTimeMillis();
                log.info("============ endTime: {}==========", endTime);

                long costTime = (endTime - rsp.getReqTs()) / 1000;
                log.info(">>>>>>>>>>>>>>> file transfer cost time: {} sec. <<<<<<<<<<<<<<<<<", costTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
