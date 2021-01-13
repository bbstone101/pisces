package com.bbstone.pisces.proto.codec;

import com.bbstone.pisces.util.BFileCodecUtil;
import com.bbstone.pisces.proto.model.BFileBase;
import com.bbstone.pisces.proto.model.BFileList;
import io.netty.buffer.ByteBuf;

public class BFileListCodec implements BFileCodec  {


    /**
     * the magic and cmd decoded by BFileCodecUtil.decode(msg)
     *  ---------------
     *  | magic | cmd |
     *  ---------------
     *  and  the rest decoded here
     *  ---------------------------------
     *  | filepath size(int) | filepath |
     *  ---------------------------------
     */
    @Override
    public BFileBase decode(ByteBuf msg) {
        BFileList bFileList = new BFileList();
        //
        String filepath = BFileCodecUtil.decodeStringField(msg);
        bFileList.setFilepath(filepath);
        //
        String fileList = BFileCodecUtil.decodeStringField(msg);
        bFileList.setFileList(fileList);

        return bFileList;
    }

    @Override
    public ByteBuf encode(BFileBase bFileBase) {
        BFileList bfile = (BFileList)bFileBase;
//        byte[] bytes = new byte[BFileCodecUtil.getObjByteSize(bfile)];


        return null;
    }
}
