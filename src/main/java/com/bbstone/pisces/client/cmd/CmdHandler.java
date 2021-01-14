package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.proto.BFileMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface CmdHandler {

    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg);

}
