package com.bbstone.pisces.comm;

public enum TaskStatus {

    STARTED(1, "started"),
    RUNNING(2, "running"),
    COMPLETED(3, "completed"),

//    ALL_RECV(1001, "All File Data Received"),
    NO_DATA(1002, "Received Empty File Data"),
    ERR_SAVE_DATA(1003, "Save File Data To Disk Error"),
    CONTINUE(1004, "Continue Append Next File Data"),


    OTHER(99999, "other");

    private int code;
    private String descp;

    TaskStatus(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    public int code() {
        return this.code;
    }

    public String descp() {
        return this.descp;
    }
}
