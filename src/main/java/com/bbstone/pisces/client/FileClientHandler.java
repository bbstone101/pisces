package com.bbstone.pisces.client;

import com.bbstone.pisces.client.base.RspDispatcher;
import com.bbstone.pisces.comm.BFileCmd;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    String serverRelativeFile = ""; //Config.serverDir;
    /**
     * Creates a client-side handler.
     */
    public FileClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // req file list
        BFileMsg.BFileReq req = BFileUtil.buildReq(BFileCmd.REQ_LIST, serverRelativeFile);
        ctx.write(req);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
    }

    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.debug("client recv total readableBytes: {}", msg.readableBytes());
        RspDispatcher.dispatch(ctx, msg);
    }
}
