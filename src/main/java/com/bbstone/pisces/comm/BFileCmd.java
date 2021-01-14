package com.bbstone.pisces.comm;

public interface BFileCmd {

    /** client send file download request */
    public static final int REQ = 1;

    /** server response file bytes(with BFile header info) */
    public static final int RSP = 2;

    /** client ack server's response **/
    public static final int RSP_ACK = 3;

    public static final int LIST = 4;

    // server register request command
    public static final String REQ_FILE = "REQ_FILE";
    public static final String REQ_LIST = "REQ_LIST";

    // client register rsp commands
    public static final String RSP_FILE = "RSP_FILE";
    public static final String RSP_DIR = "RSP_DIR";
    public static final String RSP_LIST = "RSP_LIST";
}
