package com.bbstone.pisces.server.cmd;

import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListReqHandler implements CmdHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, BFileMsg.BFileReq msg) {
        log.info("filepath: {}", msg.getFilepath());
        long reqTs = msg.getTs();
        String filepath = msg.getFilepath();
        listDir(ctx, filepath, reqTs);
    }

    /**
     * data format:
     * +---------------------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | chunk_data | delimiter |
     * +---------------------------------------------------------------------------------+
     * <p>
     * bfile_info_prefix:
     *
     */
    private void listDir(ChannelHandlerContext ctx, String filepath, long reqTs) {
        String fileTree = BFileUtil.list(filepath);
        ctx.write(fileTree);
        log.debug("fileTree: {} {}, {}", BFileUtil.LF, filepath, fileTree);

        ByteBuf rspBuf = BFileUtil.buildRspList(filepath, fileTree, reqTs);
        ctx.write(rspBuf);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
    }
}
