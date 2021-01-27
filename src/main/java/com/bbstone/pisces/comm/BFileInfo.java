package com.bbstone.pisces.comm;

import lombok.Data;

@Data
public class BFileInfo {
    //
    private String filepath;
    // D - directory, F-file
    private String fileCat;
    // file fingerprint
    private String checksum;
    // bytes
    private long fileSize;

    public BFileInfo() {
    }

    public BFileInfo(String filepath, String fileCat, String checksum, long fileSize) {
        this.filepath = filepath;
        this.fileCat = fileCat;
        this.checksum = checksum;
        this.fileSize = fileSize;
    }
}
