package com.bbstone.pisces.client.cmd;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.client.base.ClientCache;
import com.bbstone.pisces.client.base.FileRspHelper;
import com.bbstone.pisces.comm.BFileInfo;
import com.bbstone.pisces.proto.BFileMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ListRspHandler extends CmdHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        String rspData = rsp.getRspData();
        log.debug("client recv fileTree: \n / \n{}", rspData);
        List<BFileInfo> fileList = JSON.parseArray(rspData, BFileInfo.class);
        log.info("fileList: {}", JSON.toJSONString(fileList));
        ClientCache.init(fileList);
        FileRspHelper.handleNext(ctx);
    }

}
