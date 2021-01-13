package com.bbstone.pisces.client;

import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileCodecUtil;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.proto.model.BFileRequest;
import com.bbstone.pisces.proto.model.BFileResponse;
import com.bbstone.pisces.util.ConstUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //    private final ByteBuf firstMessage;
//    String srcPath = "/Users/liguifa/Downloads/test/cuizhu.jpg";
    String srcPath = "/Users/liguifa/Downloads/test/BeWater.mp4";
//    String srcPath = "/Users/liguifa/Downloads/test/tiandao01.mkv";

    //
//    String path = "/Users/liguifa/temp-cli/cuizhu-000.jpg";
        String path = "/Users/liguifa/temp-cli/BeWater-000.mp4";
//        String path = "/Users/liguifa/temp-cli/tiandao01-000.mkv";
    FileOutputStream fos = null;
    //    long fileSize = 0;// 159305889; // tiandao01.mkv
    long recvSize = 0;
//    boolean isDataHeaderRead = false;
//    String serverFileCheckSum = null;

    int chunkCounter = 0;
    int retry = 0;

    long startTime = 0;

    BFileMsg.BFileRsp rsp = null;

    /**
     * Creates a client-side handler.
     */
    public FileClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (Files.exists(Paths.get(path))) {
            Files.delete(Paths.get(path));
        }
        BFileMsg.BFileReq req = BFileMsg.BFileReq.newBuilder()
                .setMagic(ConstUtil.magic)
                .setCmd(BFileCmd.CMD_REQ)
                .setFilepath(srcPath)
                .setTs(System.currentTimeMillis())
                .build();

        ctx.write(req);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
//        startTime = System.currentTimeMillis();
    }

    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("client recv readableBytes: {}", msg.readableBytes());

        BFileMsg.BFileRsp rsp = null;
        byte[] chunkData = null;
        // --------------- decode BFileRsp & chunkData
        boolean isBFile = BFileCodecUtil.isBFileStream(msg);
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

        if (fos == null) {
            fos = new FileOutputStream(new File(path), true);
        }
        fos.write(chunkData);
        log.info("wrote current chunk to disk done.");

        chunkCounter++;
        recvSize += chunkLen;
        log.info("recv chunkLen: {}, progress: {}/{}", chunkLen, recvSize, rsp.getFileSize());
        log.info("============ chunk recv done==========");

        // all file data received
        if (recvSize > 0 && rsp.getFileSize() == recvSize) {
            log.info("all bytes received, try to close fos.");
            if (fos != null)
                fos.close();
            // check file integrity
            String checkSum = DigestUtils.md5DigestAsHex(new FileInputStream(path));
            log.info("server checksum: {}, client checksum: {}, isEq: {}", rsp.getChecksum(), checkSum, (rsp.getChecksum().equals(checkSum)));

            long endTime = System.currentTimeMillis();
            log.info("============ endTime: {}==========", endTime);

            log.info(">>>>>>>>>>>>>>> file transfer cost time: {} sec. <<<<<<<<<<<<<<<<<", (endTime - rsp.getReqTs()) / 1000);
        }
    }
}
