package com.bbstone.pisces.proto.model;

import com.bbstone.pisces.proto.BFileCmd;
import com.bbstone.pisces.util.BByteUtil;
import lombok.Data;

@Data
public class BFileList extends BFileBase {
    private String fileList;

    public BFileList() {
    }

    public BFileList(String filepath, String fileList) {
        this.filepath = filepath;
        this.filepathLen = BByteUtil.byteSize(filepath);

        this.fileList = fileList;
    }

    @Override
    public String getFilepath() {
        return super.getFilepath();
    }

    @Override
    public void setFilepath(String filepath) {
        super.setFilepath(filepath);
    }

    @Override
    public int size() {
        return baseSize() + Integer.BYTES;
    }

    @Override
    public int getCmd() {
        return BFileCmd.LIST;
    }

}
