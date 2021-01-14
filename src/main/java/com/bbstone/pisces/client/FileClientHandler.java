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
//    String srcPath = "/Users/liguifa/Downloads/test/cuizhu.jpg";
//    String srcPath = "/Users/liguifa/Downloads/test/tiandao01.mkv";
//    String srcPath = "/Users/liguifa/Downloads/test/BeWater.mp4";
    String srcPath = "/Users/liguifa/Downloads/test";


    //
//    String path = "/Users/liguifa/temp-cli/cuizhu-000.jpg";
//        String path = "/Users/liguifa/temp-cli/BeWater-000.mp4";
//        String path = "/Users/liguifa/temp-cli/tiandao01-000.mkv";


    /**
     * Creates a client-side handler.
     */
    public FileClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        BFileMsg.BFileReq req = null;
        if (Files.isDirectory(Paths.get(srcPath))) {
            // ListReq
//            req = BFileUtil.buildReq(BFileCmd.RSP_LIST, srcPath);
            req = BFileUtil.buildReq(BFileCmd.REQ_FILE, srcPath);

        } else {
            String clipath = BFileUtil.getCliFilepath(srcPath);
            if (Files.exists(Paths.get(clipath))) {
                Files.delete(Paths.get(clipath));
            }
            String temppath = BFileUtil.getCliTempFilepath(clipath);
            log.info("active-> temppath: {}", temppath);
            if (Files.exists(Paths.get(temppath))) {
                Files.delete(Paths.get(temppath));
            }
            // FileReq
            req = BFileUtil.buildReq(BFileCmd.REQ_FILE, srcPath);
        }

        ctx.write(req);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
    }

    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        RspDispatcher.dispatch(ctx, msg);
/*

        BFileMsg.BFileRsp rsp = null;
        byte[] chunkData = null;
        // --------------- decode BFileRsp & chunkData
        boolean isBFile = BFileUtil.isBFileStream(msg);
        if (isBFile) {
            // decode bfile_info_len
            int bfileInfoSize = msg.readInt();
            // decode bfile_info
            byte[] bfileInfoData = new byte[bfileInfoSize];
            msg.readBytes(bfileInfoData);
            try {
                rsp = BFileMsg.BFileRsp.parseFrom(bfileInfoData);
                // decode chunk data and set to rsp
                int chunkSize = msg.readableBytes();
                if (chunkSize == 0) {
                    log.error("chunk data is 0.");
                }
                chunkData = new byte[chunkSize];
                msg.readBytes(chunkData);
                // not work set chunkData to rsp
                //rsp.toBuilder().setFileChunkData(ByteString.copyFrom(chunkData));

            } catch (InvalidProtocolBufferException e) {
                log.error("parse BFileRs from stream error.", e);
                throw new RuntimeException("parse BFileRs from stream error.");
            }
        }
        else {
            throw new RuntimeException("recv data is not BFile format.");
        }
        // ---------------
        int chunkLen = chunkData.length;
*/


    }
}
