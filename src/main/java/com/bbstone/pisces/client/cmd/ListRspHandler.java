package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.proto.BFileMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListRspHandler implements CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
            String filepath = rsp.getFilepath();
            String checksum = rsp.getChecksum();
            String rspData = rsp.getRspData();

            log.info("client recv fileTree: \n / \n{}", rspData);
    }

}
