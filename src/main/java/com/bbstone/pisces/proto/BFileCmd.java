package com.bbstone.pisces.proto;

public interface BFileCmd {

    /** client send file download request */
    public static final int REQ = 1;

    /** server response file bytes(with BFile header info) */
    public static final int RSP = 2;

    /** client ack server's response **/
    public static final int RSP_ACK = 3;
}
