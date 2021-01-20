package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirRspHandler implements CmdHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        // server filepath
        String serverdir = rsp.getFilepath();
        String clientdir = BFileUtil.getCliFilepath(serverdir, Boolean.FALSE);
        log.info("recv server dir: {}, client dir: {}", serverdir, clientdir);
        BFileUtil.mkdir(clientdir);
        log.info("client create dir: {}", clientdir);
    }
}
