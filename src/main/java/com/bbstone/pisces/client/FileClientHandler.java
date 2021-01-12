package com.bbstone.pisces.client;

import com.bbstone.pisces.proto.BFileCodecUtil;
import com.bbstone.pisces.proto.model.BFileAck;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.proto.model.BFileRequest;
import com.bbstone.pisces.proto.model.BFileResponse;
import com.bbstone.pisces.util.ConstUtil;
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
//    String path = "/Users/liguifa/temp-cli/tiandao01-000.mkv";
    FileOutputStream fos = null;
    //    long fileSize = 0;// 159305889; // tiandao01.mkv
    long recvSize = 0;
//    boolean isDataHeaderRead = false;
//    String serverFileCheckSum = null;

    int chunkCounter = 0;
    int retry = 0;

    long startTime = 0;

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
//        String data = srcPath + ConstUtil.delimiter;
//        ctx.writeAndFlush(Unpooled.wrappedBuffer(data.getBytes(CharsetUtil.UTF_8)));

        BFileRequest bfile = new BFileRequest(srcPath);
        ctx.write(BFileCodecUtil.encode(bfile));
        ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
        startTime = System.currentTimeMillis();
    }

    // txt file OK, but jpg not ok(ByteBuf -> String -> ByteBuf) lost something?
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        BFileBase bfileBase = BFileCodecUtil.decode(msg);
        // client only recv BFileResponse
        BFileResponse bfile = (BFileResponse) bfileBase;

        // chunk data
        int len = msg.readableBytes();
        log.info("reading the {} chunk, recv chunk size: {}", (chunkCounter + 1), len);

        /*if (retry >= 3) {
            log.error("data decode error(send/recv len not match).");
            throw new RuntimeException("data decode error(send/recv len not match, or recv 0 byte.).");
        }*/

        // server sent chunk size is 0, or incoming bytes size no eq chunk size
        if (bfile.getChunkSize() == 0 || msg.readableBytes() != bfile.getChunkSize()) {
            log.error("client recv chunk size is 0, or not match server sent(cli: {}, srv: {})", len, bfile.getChunkSize());
            throw new RuntimeException("data decode error(send/recv len not match, or recv 0 byte.).");
        }
            /*retry++;
            // send ack fail, server will send the chunk again
            BFileAck bFileAck = new BFileAck(this.srcPath, ConstUtil.ACK_FAIL);
            ctx.write(BFileCodecUtil.encode(bFileAck));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));

            return;
        } else {*/

            byte[] data = new byte[msg.readableBytes()];
            msg.readBytes(data);
            if (fos == null) {
                fos = new FileOutputStream(new File(path), true);
            }
            fos.write(data);
            log.info("wrote current chunk to disk done.");

            chunkCounter++;
            recvSize += len;

            /*BFileAck bFileAck = new BFileAck(this.srcPath, ConstUtil.ACK_OK);
            ctx.write(BFileCodecUtil.encode(bFileAck));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
            log.info("send ack OK to server for next chunk.");*/
//        }

        log.info("============ chunk recv done==========");

        // all file data received
        if (recvSize > 0 && bfile.getDataLen() == recvSize) {
            log.info("all bytes received, try to close fos.");
            if (fos != null)
                fos.close();
            // check file integrity
            String checkSum = DigestUtils.md5DigestAsHex(new FileInputStream(path));
            log.info("server checksum: {}, client checksum: {}, isEq: {}", bfile.getCheckSum(), checkSum, (bfile.getCheckSum().equals(checkSum)));

            long endTime = System.currentTimeMillis();
            log.info("============ endTime: {}==========", endTime);

            log.info(">>>>>>>>>>>>>>> file transfer cost time: {} sec. <<<<<<<<<<<<<<<<<", (endTime - startTime)/1000);
        }
    }
}
