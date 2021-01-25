package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.client.base.ClientCache;
import com.bbstone.pisces.client.task.FileTask;
import com.bbstone.pisces.comm.StatusEnum;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.CtxUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;

@Slf4j
public class FileRspHandler implements CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        log.debug("handling file(id: {})...", rsp.getId());
        byte[] fileData = parseFileData(msg);
        FileTask fileTask = null;
        if ((fileTask = ClientCache.getTask(rsp.getId())) == null) {
            fileTask = new FileTask(rsp);
            ClientCache.addTask(rsp.getId(), fileTask);
        }
        StatusEnum status = fileTask.appendFileData(fileData);
        if (status == StatusEnum.COMPLETED) {
            // request next file
            String nextFile = CtxUtil.reqNextFile(ctx);
            log.info("@@@@@@@@@@@@@@@@ request next file : {} @@@@@@@@@@", nextFile);
            if (nextFile == null) {
                log.info("all files received, can stop client now(ChunkedWriteHandler need to wait).");
                ClientCache.cleanAll();
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
}
