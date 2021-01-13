package com.bbstone.pisces.util;

import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.proto.codec.BFileAckCodec;
import com.bbstone.pisces.proto.codec.BFileCodec;
import com.bbstone.pisces.proto.codec.BFileRequestCodec;
import com.bbstone.pisces.proto.codec.BFileResponseCodec;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.ConstUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

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
     * ---------------
     * | magic | cmd |
     * ---------------
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
     * @param bFileBase
     * @return
     */
    public static ByteBuf encode(BFileBase bFileBase) {
        int cmd = bFileBase.getCmd();
        BFileCodec codec = getCodec(cmd);
        return codec.encode(bFileBase);
    }

    /**
     * decode chunk must after decode BFile, at last ops of handling incomming ByteBuf at client side
     */
    public static byte[] decodeChunk(ByteBuf msg, int len) {
        byte[] data = new byte[len];
        msg.readBytes(data);
        return data;
    }

    /**
     * --------------------------
     * | field_len | str_field  |
     * --------------------------
     *
     * @param msg
     * @return
     */
    public static String decodeStringField(ByteBuf msg) {
        int bytes = msg.readInt();
        byte[] data = new byte[bytes];
        msg.readBytes(data);
        String field = BByteUtil.toStr(data);
        return field;
    }

    public static int objByteSize(Object obj) {
        int bytes = 0;

        Class clazz = obj.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        for (Field field : fieldList) {
            String fielTypeName = field.getType().getSimpleName();
            log.info("field.type.simple.name: {}", fielTypeName);
            switch (fielTypeName) {
                case "int":
                    bytes += Integer.BYTES;
                    break;
                case "Integer":
                    bytes += Integer.BYTES;
                    break;
                case "long":
                    bytes += Long.BYTES;
                    break;
                case "Long":
                    bytes += Long.BYTES;
                    break;
                case "String":
                    try {
                        bytes += String.valueOf(field.get(obj)).length();
                    } catch (IllegalAccessException e) {
                        log.error("field: {} not acessable.", e);
                    }
                    break;
                default:
                    bytes += 0;
            }
        }
        return bytes;
    }


    public static boolean isBFileStream(ByteBuf msg) {
        int len = msg.readableBytes();
        log.info("msg.len: {}", len);

        if (len < ConstUtil.bfile_info_prefix_len)
            return false;
        msg.markReaderIndex();
        byte[] data = new byte[ConstUtil.bfile_info_prefix_len];
        msg.readBytes(data);
        String prefix = BByteUtil.toStr(data);
        if (ConstUtil.bfile_info_prefix.equals(prefix)) {
            return true;
        }
        // stream not start with magic
        msg.resetReaderIndex();
        return false;
    }

    public static boolean isBFileStream(byte[] msg) {
        if (msg == null || msg.length < ConstUtil.magicLen)
            return false;
        byte[] data = new byte[ConstUtil.magicLen];
        System.arraycopy(msg, 0, data, 0, ConstUtil.magicLen);
        String magic = BByteUtil.toStr(data);
        if (ConstUtil.magic.equals(magic)) {
            return true;
        }
        return false;
    }


}
