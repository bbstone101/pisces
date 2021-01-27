package com.bbstone.pisces.client.task;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.client.base.ClientCache;
import com.bbstone.pisces.comm.BFileInfo;
import com.bbstone.pisces.comm.StatusEnum;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.CtxUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileRspHandlerHelper {

    public static void handleFileData(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        // -- zero-copy mode
        byte[] fileData = parseFileData(msg);
        FileTask fileTask = null;
        if ((fileTask = ClientCache.getTask(rsp.getId())) == null) {
            fileTask = new FileTask(rsp);
            ClientCache.addTask(rsp.getId(), fileTask);
        }
        StatusEnum status = fileTask.appendFileData(fileData);
        if (status == StatusEnum.COMPLETED) {
            log.info("file({}) transfer complete.", rsp.getFilepath());
            FileRspHandlerHelper.handleNext(ctx);
            // request next file
//            String nextFile = CtxUtil.reqNextFile(ctx);
//            log.info("@@@@@@@@@@@@@@@@ request next file : {} @@@@@@@@@@", nextFile);
//            if (nextFile == null) {
//                log.info("all files received, can stop client now(ChunkedWriteHandler need to wait).");
//                ClientCache.cleanAll();
//                System.exit(0);
//            }
        }
    }

    private static byte[] parseFileData(ByteBuf msg) {
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

    public static void handleNext(ChannelHandlerContext ctx) {
        BFileInfo nextFile = CtxUtil.reqNextFile(ctx);
        log.info("@@@@@@@@@@@@@@@@ request next file : {} @@@@@@@@@@", JSON.toJSONString(nextFile));
        if (nextFile == null) {
            log.info("all files received, can stop client now(ChunkedWriteHandler need to wait).");
            ClientCache.cleanAll();
            System.exit(0);
        }
    }
}
