package com.bbstone.pisces.proto.model;


import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.util.BByteUtil;
import lombok.Data;

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
@Data
public class BFileAck extends BFileBase {
    private int ack;

    public BFileAck() {
    }

    public BFileAck(String filepath, int ack) {
        this.filepath = filepath;
        this.filepathLen = BByteUtil.byteSize(filepath);
        this.ack = ack;
    }

    @Override
    public int getCmd() {
        return BFileCmd.RSP_ACK;
    }

    @Override
    public int size() {
        return baseSize() + Integer.BYTES;
    }
}
