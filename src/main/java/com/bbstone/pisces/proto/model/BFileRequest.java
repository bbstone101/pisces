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
    private int cmd = BFileCmd.REQ;

    public BFileRequest() {
    }

    public BFileRequest(String filepath) {
        this.filepath = filepath;
        this.filepathLen = BByteUtil.byteSize(filepath);
    }


    @Override
    public int getCmd() {
        return cmd;
    }

    @Override
    public int size() {
        return baseSize();
    }
}
