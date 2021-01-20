package com.bbstone.pisces.client;

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

import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    //    private final ByteBuf firstMessage;
//    String srcPath = "/Users/bbstone/Downloads/test/cuizhu.jpg";
//    String srcPath = "/Users/bbstone/Downloads/test/tiandao01.mkv";
//    String srcPath = "/Users/bbstone/Downloads/test/BeWater.mp4";
//    String srcPath = "/Users/bbstone/Downloads/test";

    String serverRelativeFile = ""; //Config.serverDir;


    //
//    String path = "/Users/bbstone/temp-cli/cuizhu-000.jpg";
//        String path = "/Users/bbstone/temp-cli/BeWater-000.mp4";
//        String path = "/Users/bbstone/temp-cli/tiandao01-000.mkv";


    /**
     * Creates a client-side handler.
     */
    public FileClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        // req dir
        /*
        BFileMsg.BFileReq req = null;
        if (Files.isDirectory(Paths.get(BFileUtil.getClientFullPath(serverRelativeFile)))) {
            // ListReq
//            req = BFileUtil.buildReq(BFileCmd.RSP_LIST, srcPath);
            req = BFileUtil.buildReq(BFileCmd.REQ_FILE, serverRelativeFile);

        } else {
            String clipath = BFileUtil.getCliFilepath(serverRelativeFile);
            if (Files.exists(Paths.get(clipath))) {
                Files.delete(Paths.get(clipath));
            }
            String temppath = BFileUtil.getCliTempFilepath(clipath);
            log.info("active-> temppath: {}", temppath);
            if (Files.exists(Paths.get(temppath))) {
                Files.delete(Paths.get(temppath));
            }
            // FileReq
            req = BFileUtil.buildReq(BFileCmd.REQ_FILE, serverRelativeFile);
        }
        ctx.write(req);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
*/
        // req file list
        BFileMsg.BFileReq req = BFileUtil.buildReq(BFileCmd.REQ_LIST, serverRelativeFile);
        ctx.write(req);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
    }

    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("client recv readableBytes: {}", msg.readableBytes());
        RspDispatcher.dispatch(ctx, msg);
    }
}
