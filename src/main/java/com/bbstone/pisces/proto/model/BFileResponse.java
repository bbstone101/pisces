package com.bbstone.pisces.proto.model;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.proto.BFileCmd;
import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * File header info(magicCode, filepath, checksum)
 * File data: direct append from FileRegion to netty ChannelHandlerContext via
 *  ctx.write(new DefaultFileRegion(new File(filepath), pos, chunkSize));
 *
 */

/**
 * BFileBase
 * -------------------------------------------
 * | magic | cmd | filepathLen | filepath    |
 * -------------------------------------------
 *
 * BFileResponse(include BFileBase)
 * ---------------------------------------------------------------------------------------
 * | magic | cmd | filepathLen | filepath | checkSumLen | checkSum | dataLen | chunkSize |
 * ---------------------------------------------------------------------------------------
 *
 */
@Data
public class BFileResponse extends BFileBase {
    //
    private int checkSumLen;
    private String checkSum;

    // transferred file total bytes size
    private long dataLen;

    // the transferred file chunk size(default 8192) can be (0,8192]
    private long chunkSize;

    public BFileResponse() {
    }

    public BFileResponse(String filepath, String checkSum, long dataLen, long chunkSize) {
        this.filepath = filepath;
        this.checkSum = checkSum;
        this.dataLen = dataLen;
        this.chunkSize = chunkSize;
    }

    public BFileResponse(int filepathLen, String filepath, int checkSumLen, String checkSum, long dataLen, long chunkSize) {
        this.filepathLen = filepathLen;
        this.filepath = filepath;
        this.checkSumLen = checkSumLen;
        this.checkSum = checkSum;
        this.dataLen = dataLen;
        this.chunkSize = chunkSize;
    }

//    public String getMagicCode() {
//        return this.magicCode;
//    }


    public String toString() {
        return JSON.toJSONString(this);
    }


    @Override
    public int size() {

        return baseSize() + Integer.BYTES + checkSumLen + Long.BYTES + Long.BYTES;
    }

    @Override
    public int getCmd() {
        return BFileCmd.RSP;
    }
}
