package com.bbstone.pisces.proto;


import com.bbstone.pisces.proto.model.BFileAck;
import com.bbstone.pisces.proto.model.BFileRequest;
import com.bbstone.pisces.proto.model.BFileResponse;
import com.bbstone.pisces.util.BByteUtil;

public class BFileBuilder {

    public static BFileResponse buildRsp(String filepath, String checksum, long dataLen, long chunkSize) {
        return new BFileResponse(BByteUtil.byteSize(filepath), filepath, BByteUtil.byteSize(checksum), checksum, dataLen, chunkSize);
    }

    public static BFileRequest buildReq(String filepath) {
        return new BFileRequest(filepath);
    }

    public static BFileAck buildAck(String filepath, int ack) {
        return new BFileAck(filepath, ack);
    }



}
