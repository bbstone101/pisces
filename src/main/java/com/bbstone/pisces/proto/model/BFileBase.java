package com.bbstone.pisces.proto.model;


import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.ConstUtil;

/**
 * -------------------------------------------
 * | magic | cmd | filepathLen | filepath    |
 * -------------------------------------------
 *
 */
public abstract class BFileBase {
    // identify current transferred  stream is BFile
    protected String magicCode = BByteUtil.magic(); //"BBSTONE_BFile";
    protected int cmd;

    // bytes of : /aa/tiandao01.mkv
    protected int filepathLen;

    // e.g. /Users/liguifa/Downloads/test/aa/tiandao01.mkv
    // filepath: /aa/tiandao01.mkv
    protected String filepath;

    public String getMagicCode() {
        return magicCode;
    }

    public void setMagicCode(String magicCode) {
        this.magicCode = magicCode;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
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

    public abstract int size();
    public int baseSize() {
        return ConstUtil.magicLen + 4 + 4 + filepathLen;
    }
}
