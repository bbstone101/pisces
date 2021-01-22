package com.bbstone.pisces.client.cmd;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.client.storage.ClientCache;
import com.bbstone.pisces.comm.BFileCmd;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.ConstUtil;
import com.bbstone.pisces.util.CtxUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ListRspHandler implements CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
//            String filepath = rsp.getFilepath();
//            String checksum = rsp.getChecksum();
            String rspData = rsp.getRspData();
            log.debug("client recv fileTree: \n / \n{}", rspData);

        List<String> fileList = JSON.parseArray(rspData, String.class);
        ClientCache.init(fileList);

        CtxUtil.reqNextFile(ctx);
    }

}
