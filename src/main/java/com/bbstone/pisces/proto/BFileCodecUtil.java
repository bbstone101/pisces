package com.bbstone.pisces.proto;

import com.bbstone.pisces.proto.codec.BFileAckCodec;
import com.bbstone.pisces.proto.codec.BFileCodec;
import com.bbstone.pisces.proto.codec.BFileRequestCodec;
import com.bbstone.pisces.proto.codec.BFileResponseCodec;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BFileCodecUtil {

    private static final Map<Integer, BFileCodec> codecMap = new HashMap<>();
    public static void init() {
        register(BFileCmd.REQ, new BFileRequestCodec());
        register(BFileCmd.RSP, new BFileResponseCodec());
        register(BFileCmd.RSP_ACK, new BFileAckCodec());


    }

    private static void register(Integer cmd, BFileCodec codec) {
        codecMap.put(cmd, codec);
    }

    private static BFileCodec getCodec(int cmd) {
        return codecMap.get(cmd);
    }

    /**
     * this decode(msg) will decode magic and cmd, others decoded by sub-codec
     *  ---------------
     *  | magic | cmd |
     *  ---------------
     *
     * @param msg
     * @return
     */
    public static BFileBase decode(ByteBuf msg) {
        // parse magic
        byte[] data = new byte[ConstUtil.magicLen];
        msg.readBytes(data);
        String magic = BByteUtil.toStr(data);
        if (!ConstUtil.magic.equals(magic)) {
            log.info("bad magic: {}", magic);
            throw new RuntimeException("not bfile byte data.");
        }
        // select codec
        int cmd = msg.readInt();
        BFileCodec codec = getCodec(cmd);
        return codec.decode(msg);
    }

    /**
     *
     * @param bFileBase
     * @return
     */
    public static ByteBuf encode(BFileBase bFileBase) {
//        CompositeByteBuf buf = Unpooled.compositeBuffer();
//        buf.addComponent(true, BByteUtil.toByteBuf(ConstUtil.magic));
//        buf.addComponent(true, Unpooled.buffer(4).writeInt(bfile.getFilepathLen()));
//        buf.addComponent(true, Unpooled.wrappedBuffer(bfile.getFilepath().getBytes(CharsetUtil.UTF_8)));
//        buf.addComponent(true, Unpooled.buffer(4).writeInt(bfile.getCheckSumLen()));
//        buf.addComponent(true, Unpooled.wrappedBuffer(bfile.getCheckSum().getBytes(CharsetUtil.UTF_8)));
//        buf.addComponent(true, Unpooled.buffer(8).writeLong(bfile.getDataLen()));

        int cmd = bFileBase.getCmd();
        BFileCodec codec = getCodec(cmd);
        return codec.encode(bFileBase);


    }

    /** decode chunk must after decode BFile, at last ops of handling incomming ByteBuf at client side*/
    public static byte[] decodeChunk(ByteBuf msg, int len) {

        byte[] data = new byte[len];
        msg.readBytes(data);
        return data;
    }

}
