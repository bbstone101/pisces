package com.bbstone.pisces.client.cmd;

import com.bbstone.pisces.client.base.ClientCache;
import com.bbstone.pisces.client.base.FileRspHandlerHelper;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BByteUtil;
import com.twmacinta.util.MD5;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileRspHandler extends CmdHandler {

    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileRsp rsp, ByteBuf msg) {
        log.info("handling file(id: {})...", rsp.getId());

        // ssl enabled, receiving a file, server only send BFileRsp info once
        if (Config.sslEnabled) { // ChunkedFile mode
            String recvFileKey = MD5.asHex(BByteUtil.toBytes(rsp.getFilepath()));
            ClientCache.addRspInfo(recvFileKey, rsp);
            return; // subsequent chunked file data will be handle by ChunkedFileDataHandler before this
        }

        FileRspHandlerHelper.handleFileData(ctx, rsp, msg);
    }



}
