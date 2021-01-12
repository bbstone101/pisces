package com.bbstone.pisces.proto.model;


import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.util.BByteUtil;

/**
 *
 * when client receive server sent chunk, will check the chunk,
 * if not correct chunk size, send ack(1- wrong chunk size),
 * server will send the previous fail chunk again and again utile ack(0- right chunk size).
 *
 *
 * BFileBase
 * -------------------------------------------
 * | magic | cmd | filepathLen | filepath    |
 * -------------------------------------------
 *
 * BFileAck(include BFileBase)
 * ----------------------------------------------
 * | magic | cmd | filepathLen | filepath | ack |
 * ----------------------------------------------
 *
 */
public class BFileAck extends BFileBase {
    private int cmd = BFileCmd.RSP_ACK;
    private int ack;

    public BFileAck() {
    }

    public BFileAck(String filepath, int ack) {
        this.filepath = filepath;
        this.filepathLen = BByteUtil.byteSize(filepath);
        this.ack = ack;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    @Override
    public int getCmd() {
        return cmd;
    }

    @Override
    public int size() {
        return baseSize() + 4;
    }
}
