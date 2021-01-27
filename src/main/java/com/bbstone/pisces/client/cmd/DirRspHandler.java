package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.client.task.FileRspHandlerHelper;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.CtxUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirRspHandler extends CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        // server filepath
        String serverdir = rsp.getFilepath();
        String clientdir = BFileUtil.getClientFullPathWithCheck(serverdir, Boolean.FALSE);
        log.debug("recv server dir: {}, client dir: {}", serverdir, clientdir);
        BFileUtil.mkdir(clientdir);
        log.debug("client create dir: {}", clientdir);

        FileRspHandlerHelper.handleNext(ctx);
//        String nextFile = CtxUtil.reqNextFile(ctx);
//        log.debug("@@@@@@@@@@@@@@@@ request next file : {} @@@@@@@@@@", nextFile);
//        if (nextFile == null) {
//            log.debug("all files received, try to stop client.");
//            System.exit(0);
//        }
    }
}
