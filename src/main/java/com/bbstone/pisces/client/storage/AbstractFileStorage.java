package com.bbstone.pisces.client.storage;

import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BFileUtil;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public abstract class AbstractFileStorage {

    private int spos = 0;
    // storage chunk counter
    private int scc = 8;

    // 8k * 8
    private final int SBUF_SIZE = 8192 * scc;
    // 1M store buffer, accumulate 1m before save file data
    private byte[] sbuf = new byte[SBUF_SIZE];

    private long fileSize = 0;
    private long recvSize = 0;
    // every 10 chunks write disk once
    private int chunkCounter = 0;

    FileOutputStream fos = null;
    String clipath = null;
    String temppath = null;
    File tempfile = null;

    private void setInfo(BFileMsg.BFileRsp rsp) {
        fileSize = rsp.getFileSize();

        String filepath = rsp.getFilepath();
        clipath = BFileUtil.getClientFullPathWithCheck(filepath);
        temppath = BFileUtil.getClientTempFileFullPath(clipath);
        log.info("filepath: {}, clipath: {}, temppath: {}", filepath, clipath, temppath);
        tempfile = new File(temppath);
    }

    public void store(BFileMsg.BFileRsp rsp, ByteBuf buf) {
        setInfo(rsp);
        if (chunkCounter > 0 && chunkCounter % scc == 0) {

        } else {

        }
    }

    private void append(ByteBuf buf) {
        // read data from ByteBuf
        int dataLen = buf.readableBytes();
        byte[] data = new byte[dataLen];
        buf.readBytes(data);
        // append data to storage buffer(sbuf)
        System.arraycopy(data, 0, sbuf, spos, dataLen);
        // increase sbuf next write pos
        spos += dataLen;
    }

    private void saveData(byte[] data) {
        try {
            if (fos == null) {
                fos = new FileOutputStream(tempfile, true);
            }
            fos.write(data);
            log.info("wrote data to disk ...");
            if (recvSize == fileSize) {
                fos.close();
                log.info("all file data saved.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
