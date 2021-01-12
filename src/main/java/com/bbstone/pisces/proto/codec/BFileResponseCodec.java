package com.bbstone.pisces.proto.codec;

import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.proto.model.BFileResponse;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BFileResponseCodec implements BFileCodec {

    /**
     * the magic and cmd decoded by BFileCodecUtil.decode(msg)
     *  ---------------
     *  | magic | cmd |
     *  ---------------
     *
     *  and  the rest decoded here
     *  -------------------------------------------------------------------------
     *  | filepathLen | filepath | checkSumLen | checksum | dataLen | chunkSize |
     *  -------------------------------------------------------------------------
     *
     *  the chunk data(send from server by FileRegion) will be decoded by BFileCodec.decodeChunk(msg)
     */
    @Override
    public BFileBase decode(ByteBuf msg) {
        BFileResponse bfile = new BFileResponse();

        // parse filepathLen
        int filepathLen = msg.readInt();
        bfile.setFilepathLen(filepathLen);
        // parse filepath
        byte[] data = new byte[filepathLen];
        msg.readBytes(data);
        String filepath = BByteUtil.toStr(data);
        bfile.setFilepath(filepath);

        // parse file checksumLen
        int checksumLen = msg.readInt();
        bfile.setCheckSumLen(checksumLen);
        // parse file checksum
        data = new byte[checksumLen];
        msg.readBytes(data);
        String checksum = BByteUtil.toStr(data);
        bfile.setCheckSum(checksum);

        // parse file total dataLen
        long dataLen = msg.readLong();
        bfile.setDataLen(dataLen);

        // parse chunkSize
        long chunkSize = msg.readLong();
        bfile.setChunkSize(chunkSize);

        return bfile;
    }

    /**
     * encode BFileResponse to following byte[] format:
     *  ----------------------------------------------------------------------------------------
     *  | magic | cmd | filepathLen | filepath | checkSumLen | checksum | dataLen | chunkSize |
     *  ----------------------------------------------------------------------------------------
     *
     *  the chunk data(send from server by FileRegion) directly send from file channel to socket channel
     */
    @Override
    public ByteBuf encode(BFileBase bFileBase) {
        BFileResponse bfile = (BFileResponse)bFileBase;
        //        byte[] bytes = new byte[ConstUtil.magicLen + 4 + bfile.getFilepathLen() + 4 + bfile.getCheckSumLen()];
        // len(magic) + len(cmd) + len(filepath) + len(checkSum) + len(dataLen)
//        byte[] bytes = new byte[ConstUtil.magicLen + 4 + 4 + bfile.getFilepathLen() + 4 + bfile.getCheckSumLen() + 8];

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
        // checkSumLen
        destPos += bfile.getFilepathLen();
        byte[] checksumLenBytes = BByteUtil.toBytes(bfile.getCheckSumLen());
        System.arraycopy(checksumLenBytes, 0, bytes, destPos, 4); // int(4bytes)
        // checkSum
        destPos += 4;
        byte[] checksumBytes = BByteUtil.toBytes(bfile.getCheckSum());
        System.arraycopy(checksumBytes, 0, bytes, destPos, bfile.getCheckSumLen());

        // checkSumLen
        destPos += bfile.getCheckSumLen();
        byte[] dataLenBytes = BByteUtil.toBytes(bfile.getDataLen());
        System.arraycopy(dataLenBytes, 0, bytes, destPos, 8); // long(8bytes)

        // chunkSize
        destPos += 8;
        byte[] chunkSizeBytes = BByteUtil.toBytes(bfile.getChunkSize());
        System.arraycopy(chunkSizeBytes, 0, bytes, destPos, 8); // long(8bytes)
        // wrap bytes to ByteBuf
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        return buf;
    }
}
