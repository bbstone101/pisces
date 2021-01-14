package com.bbstone.pisces.server.cmd;

import com.bbstone.pisces.proto.BFileMsg;
import io.netty.channel.ChannelHandlerContext;

public interface CmdHandler {

    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileReq msg);

}
