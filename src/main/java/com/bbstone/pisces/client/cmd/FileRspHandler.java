package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.client.storage.ClientCache;
import com.bbstone.pisces.client.task.RecvTask;
import com.bbstone.pisces.comm.TaskStatus;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.CtxUtil;
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
        log.info("handling file(id: {})...", rsp.getId());
        byte[] fileData = parseFileData(msg);
        RecvTask recvTask = null;
        if ((recvTask = ClientCache.getTask(rsp.getId())) == null) {
            recvTask = new RecvTask(rsp);
            ClientCache.addTask(rsp.getId(), recvTask);
        }
        TaskStatus status = recvTask.appendFileData(fileData);
        if (status == TaskStatus.COMPLETED) {
            // request next file
            String nextFile = CtxUtil.reqNextFile(ctx);
            log.info("@@@@@@@@@@@@@@@@ request next file : {} @@@@@@@@@@", nextFile);
            if (nextFile == null) {
                log.info("all files received, try to stop client.");
                System.exit(0);
            }
        }
    }


    private byte[] parseFileData(ByteBuf msg) {
        byte[] fileData = null;
        // decode chunk data and set to rsp
        int chunkSize = msg.readableBytes();
        if (chunkSize == 0) {
            log.error("chunk data is 0.");
            return new byte[0];
        }
        fileData = new byte[chunkSize];
        msg.readBytes(fileData);
        return fileData;
    }


//    byte[] chunkData = null;
//    // decode chunk data and set to rsp
//    int chunkSize = msg.readableBytes();
//        if (chunkSize == 0) {
//        log.error("chunk data is 0.");
//    }
//    chunkData = new byte[chunkSize];
//        msg.readBytes(chunkData);
//    // not work set chunkData to rsp
//    //rsp.toBuilder().setFileChunkData(ByteString.copyFrom(chunkData));
//    // ---------------
//    int chunkLen = chunkData.length;
//
//        try {
//        String filepath = rsp.getFilepath();
//        clipath = BFileUtil.getCliFilepath(filepath);
//        temppath = BFileUtil.getClientTempFileFullPath(clipath);
//        log.info("filepath: {}, clipath: {}, temppath: {}", filepath, clipath, temppath);
//        tempfile = new File(temppath);
//
//        if (fos == null) {
//            fos = new FileOutputStream(tempfile, true);
//        }
////            FileOutputStream fos = new FileOutputStream(tempfile, true);
//        fos.write(chunkData);
//        log.info("wrote current chunk to disk done.");
//
//        chunkCounter++;
//        recvSize += chunkLen;
//        log.info("recv the {} chunk, chunkLen: {}, progress: {}/{}", chunkCounter, chunkLen, recvSize, rsp.getFileSize());
//        log.info("============ chunk recv done==========");
//
//        // all file data received
//        if (recvSize > 0 && rsp.getFileSize() == recvSize) {
//            log.info("all bytes received, try to close fos.");
//            recvSize = 0; // reset counter
//            chunkCounter = 0;
//            if (fos != null)
//                fos.close();
//            fos = null;
//
//            // check file integrity
//            String checkSum = BFileUtil.checksum(new File(temppath));
//            log.info("server checksum: {}, client checksum: {}, isEq: {}", rsp.getChecksum(), checkSum, (rsp.getChecksum().equals(checkSum)));
//
//            if (rsp.getChecksum().equals(checkSum)) {
//                BFileUtil.renameCliTempFile(tempfile, clipath);
//            }
//            long endTime = System.currentTimeMillis();
//            log.info("============ endTime: {}==========", endTime);
//
//            long costTime = (endTime - rsp.getReqTs()) / 1000;
//            log.info(">>>>>>>>>>>>>>> file transfer cost time: {} sec. <<<<<<<<<<<<<<<<<", costTime);
//
//            // request next file
//            String nextFile = CtxUtil.reqNextFile(ctx);
//            log.info("@@@@@@@@@@@@@@@@ request next file : {} @@@@@@@@@@", nextFile);
//            if (nextFile == null) {
//                log.info("all files received, try to stop client.");
//                System.exit(0);
//            }
////                String nextServerFullPath = CtxUtil.reqNextFile(ctx);
////                String partFilePath = BFileUtil.getPartFileFromFull(nextServerFullPath);
//        }
//    } catch (IOException e) {
//        log.error("save file error.", e);
//    } finally {
//            /*if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }*/
//    }

}
