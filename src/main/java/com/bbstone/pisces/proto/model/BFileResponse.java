package com.bbstone.pisces.proto.model;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.proto.BFileCmd;

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
public class BFileResponse extends BFileBase {
    // identify current trasfer stream is BFile
//    private int magicCodeLen = BByteUtil.magicLen(); //"BBSTONE_BFile".getBytes(CharsetUtil.UTF_8).length;
//    private String magicCode = BByteUtil.magic(); //"BBSTONE_BFile"

    private int cmd = BFileCmd.RSP;
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

    public String getMagicCode() {
        return this.magicCode;
    }

    public int getFilepathLen() {
        return filepathLen;
    }

    public void setFilepathLen(int filepathLen) {
        this.filepathLen = filepathLen;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public int size() {
        return baseSize() + 4 + checkSumLen + 8 + 8;
    }

    public int getCheckSumLen() {
        return checkSumLen;
    }

    public void setCheckSumLen(int checkSumLen) {
        this.checkSumLen = checkSumLen;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Override
    public int getCmd() {
        return cmd;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
