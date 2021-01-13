package com.bbstone.pisces.proto.model;


import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.util.BByteUtil;

/**
 * BFileBase
 * -------------------------------------------
 * | magic | cmd | filepathLen | filepath    |
 * -------------------------------------------
 *
 * BFileRequest(include BFileBase)
 * --------------------------------------------------------------------------
 * | magic | cmd | filepathLen | filepath |
 * --------------------------------------------------------------------------
 *
 */
public class BFileRequest extends BFileBase {

    public BFileRequest() {
    }

    public BFileRequest(String filepath) {
        this.filepath = filepath;
        this.filepathLen = BByteUtil.byteSize(filepath);
    }


    @Override
    public int getCmd() {
        return BFileCmd.REQ;
    }

    @Override
    public int size() {
        return baseSize();
    }


    @Override
    public int getFilepathLen() {
        return filepathLen;
    }

    @Override
    public void setFilepathLen(int filepathLen) {
        setFilepathLen(filepathLen);
    }

    @Override
    public String getFilepath() {
        return filepath;
    }

    @Override
    public void setFilepath(String filepath) {
        setFilepath(filepath);
    }
}
