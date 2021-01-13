package com.bbstone.pisces.client;

import com.bbstone.pisces.proto.BFileMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoClientHandler extends SimpleChannelInboundHandler<BFileMsg.BFileRsp> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BFileMsg.BFileRsp msg) throws Exception {
        log.info("magic from server: {}", msg.getMagic());


    }
}
