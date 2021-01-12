package com.bbstone.pisces.proto.codec;

import com.bbstone.pisces.proto.model.BFileAck;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BFileAckCodec implements BFileCodec {

    /**
     * the magic and cmd decoded by BFileCodecUtil.decode(msg)
     *  ---------------
     *  | magic | cmd |
     *  ---------------
     *  and  the rest decoded here
     *  --------------------------------
     *  | filepathLen | filepath | ack |
     *  --------------------------------
     */
    @Override
    public BFileBase decode(ByteBuf msg) {
        BFileAck bfile = new BFileAck();

        // parse filepathLen
        int filepathLen = msg.readInt();
        bfile.setFilepathLen(filepathLen);

        // decode filepath
        byte[] data = new byte[filepathLen];
        msg.readBytes(data);
        String filepath = BByteUtil.toStr(data);
        bfile.setFilepath(filepath);

        // decode ack
        int ack = msg.readInt();
        bfile.setAck(ack);

        return bfile;
    }

    /**
     * encode BFileRequest to following byte[] format:
     *  ----------------------------------------------
     *  | magic | cmd | filepathLen | filepath | ack |
     *  ----------------------------------------------
     *
     */
    @Override
    public ByteBuf encode(BFileBase bFileBase) {
        BFileAck bfile = (BFileAck)bFileBase;
        byte[] bytes = new byte[bfile.size()];

        // magic code
        int destPos = 0;
        byte[] magicBytes = BByteUtil.toBytes(ConstUtil.magic);
        System.arraycopy(magicBytes, 0, bytes, destPos, ConstUtil.magicLen);

        // cmd
        destPos += ConstUtil.magicLen;
        byte[] cmdBytes = BByteUtil.toBytes(bfile.getCmd());
        System.arraycopy(cmdBytes, 0, bytes, destPos, 4); // int(4bytes)

        // filepathLen
        destPos += 4;
        byte[] filepathLenBytes = BByteUtil.toBytes(bfile.getFilepathLen());
        System.arraycopy(filepathLenBytes, 0, bytes, destPos, 4); // int(4bytes)

        // filepath
        destPos += 4;
        byte[] filepathBytes = BByteUtil.toBytes(bfile.getFilepath());
        System.arraycopy(filepathBytes, 0, bytes, destPos, bfile.getFilepathLen());

        // ack
        destPos += bfile.getFilepathLen();
        byte[] ackBytes = BByteUtil.toBytes(bfile.getAck());
        System.arraycopy(ackBytes, 0, bytes, destPos, 4); // int(4bytes)

        // wrap bytes to ByteBuf
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        return buf;
    }
}
