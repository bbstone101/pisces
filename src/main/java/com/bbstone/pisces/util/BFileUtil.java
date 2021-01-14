package com.bbstone.pisces.util;

import com.bbstone.pisces.comm.BFileCmd;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class BFileUtil {

    public static final String LF = "\n";

    private static int level = -1;

    public static boolean isBFileStream(ByteBuf msg) {
        int len = msg.readableBytes();
        log.info("msg.len: {}", len);

        if (len < ConstUtil.bfile_info_prefix_len)
            return false;
        msg.markReaderIndex();
        byte[] data = new byte[ConstUtil.bfile_info_prefix_len];
        msg.readBytes(data);
        String prefix = BByteUtil.toStr(data);
        if (ConstUtil.bfile_info_prefix.equals(prefix)) {
            return true;
        }

        // stream not start with magic
        msg.resetReaderIndex();
        return false;
    }

    public static boolean isBFileStream(byte[] msg) {
        if (msg == null || msg.length < ConstUtil.magicLen)
            return false;
        byte[] data = new byte[ConstUtil.magicLen];
        System.arraycopy(msg, 0, data, 0, ConstUtil.magicLen);
        String magic = BByteUtil.toStr(data);
        if (ConstUtil.magic.equals(magic)) {
            return true;
        }
        return false;
    }





    public static String list(String filepath) {
        if (Files.notExists(Paths.get(filepath))) {
            log.warn("not found file/directory: {}", filepath);
        }
        StringBuilder sbu = new StringBuilder();
        File file = new File(filepath);
        if (Files.isDirectory(Paths.get(filepath))) {
            level = -1; // reset
            listAll(sbu, new File(filepath));
        } else {
            sbu.append(file.getName());
        }
        return sbu.toString();
    }

    private static void listAll(StringBuilder sbu, File file) {
        level++;
        File[] flist = file.listFiles();
        Arrays.sort(flist);
        for (File subfile : flist) {
            for (int i = 0; i < level; i++) {
                sbu.append("|   ");
            }
            sbu.append("|-- " + subfile.getName()).append(LF);
            if (subfile.isDirectory()) {
                listAll(sbu, subfile);
            }
        }
        level--;
    }

    public static String checksum(String filepath) {
        try {
            return DigestUtils.md5DigestAsHex(new FileInputStream(filepath));
        } catch (IOException e) {
            log.error("calc file hash fail.", e);
//            throw new RuntimeException("calc file hash fail.", e);
        }
        return null;
    }

    public static String checksum(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     *
     * @param filepath - server full path
     * @return
     */
    public static String getCliFilepath(String filepath) {
        String relativePath = filepath.substring(Config.serverDir.length());
        return Config.clientDir + relativePath;
    }

    /**
     *
     * @param clipath - client full path
     * @return
     */
    public static String getCliTempFilepath(String clipath) {
        return clipath + Config.tempFilePostfix;
    }

    /**
     * rename temp file to client file
     * @param tmpFile
     * @param clipath
     */
    public static void renameCliTempFile(File tmpFile, String clipath) {
        tmpFile.renameTo(new File(clipath));
    }


    public static void main(String[] args) {
        String filepath = "/Users/liguifa/Downloads/test";
        String files = list(filepath);
        log.info(LF + filepath + LF + files);


        String srvpath = "/Users/liguifa/Downloads/test/cuizhu.jpg";
        System.out.println(srvpath);
        String clipath = getCliFilepath(srvpath);
        System.out.println("resutl: " + clipath);
        String clitempath = getCliTempFilepath(clipath);
        System.out.println("resutl: " + clitempath);

        String checkSum = null;
        try {
            checkSum = DigestUtils.md5DigestAsHex(new FileInputStream(srvpath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(checkSum);
    }


    /**
     * build FileRsp
     *
     * @param filepath
     * @param filesize
     * @param checksum
     * @param reqTs
     * @return
     */
    public static ByteBuf buildRspFile(String filepath, long filesize, String checksum, long reqTs) {
        return buildBFileReq(BFileCmd.RSP_FILE, filepath, filesize, checksum, null, null, reqTs);
    }

    /**
     * build ListRsp
     * @param filepath
     * @param rspData
     * @param reqTs
     * @return
     */
    public static ByteBuf buildRspList(String filepath, String rspData, long reqTs) {
        // checksum: rspData 's md5 hash
        String checksum = DigestUtils.md5DigestAsHex(BByteUtil.toBytes(rspData));
        return buildBFileReq(BFileCmd.RSP_LIST, filepath, 0, checksum, rspData, null, reqTs);
    }


    /**
     *
     * BFileRsp intro:
     * it contains 2 field for store server response data(rspData/chunkData),
     * usage:
     *  rspData - string, can be json string or only some string words
     *  fileChunkData - byte[], byte array data
     *
     *
     * Standard Rsp format like:
     * +--------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | delimiter |
     * +--------------------------------------------------------------------+
     * <p>
     *
     * Non-standard format(append byte[] data after Rsp object, because cannot retrieve the data and set to Rsp.chunkData:
     * e.g. FileRsp(cmd: CMD_REQ) data format(only for FileRegion which directly write file data to channel):
     * +---------------------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | chunk_data | delimiter |
     * +---------------------------------------------------------------------------------+
     * <p>
     * bfile_info_prefix:
     *
     * Note: delimiter not encode in upper format, need append delimiter after
     *      this method invoked, see "bytes size format" in method body
     *
     * @param cmd
     * @param filepath
     * @param filesize
     * @param checksum
     * @param rspData
     * @param chunkData
     * @param reqTs
     * @return
     */
    private static ByteBuf buildBFileReq(String cmd, String filepath, long filesize, String checksum, String rspData, byte[] chunkData, long reqTs) {
        // BFile info prefix
        byte[] prefix = BByteUtil.toBytes(ConstUtil.bfile_info_prefix);
        // BFile info
        BFileMsg.BFileRsp rsp = BFileMsg.BFileRsp.newBuilder()
                .setMagic(ConstUtil.magic)
                .setCmd(cmd) //BFileCmd.CMD_RSP)
                .setFilepath(filepath)
                .setFileSize(filesize)
                .setChecksum(checksum)
                .setRspData(StringUtils.trimToEmpty(rspData))
                .setChunkData(chunkData == null ? ByteString.EMPTY : ByteString.copyFrom(chunkData))
                .setReqTs(reqTs)
                .setRspTs(System.currentTimeMillis())
                .build();
        byte[] rspWithoutFileData = rsp.toByteArray();

        int bfileInfoLen = rspWithoutFileData.length;
        byte[] bifileInfoBytes = BByteUtil.toBytes(bfileInfoLen);

        /**
         * bytes size format:
         * +----------------------------------------------------------------+
         * | bfile_info_prefix_len | bfile_info_bytes(int) | bfile_info_len |
         * +----------------------------------------------------------------+
         *
         */
        // assemble data
        int cpos = 0;
        byte[] data = new byte[prefix.length + Integer.BYTES + rspWithoutFileData.length];
        // bfile_info_prefix
        System.arraycopy(prefix, 0, data, cpos, prefix.length);
        // bfile_info size
        cpos += prefix.length;
        System.arraycopy(bifileInfoBytes, 0, data, cpos, Integer.BYTES);
        // bfile_info
        cpos += Integer.BYTES;
        System.arraycopy(rspWithoutFileData, 0, data, cpos, rspWithoutFileData.length);

        log.info("data.len: {}, bytes: {}", data.length, data.toString());
        return Unpooled.wrappedBuffer(data);
    }


    /**
     *
     * @param cmd
     * @param filepath
     * @return
     */
    public static BFileMsg.BFileReq buildReq(String cmd, String filepath) {
        BFileMsg.BFileReq req = BFileMsg.BFileReq.newBuilder()
                .setMagic(ConstUtil.magic)
                .setCmd(cmd)
                .setFilepath(filepath)
                .setTs(System.currentTimeMillis())
                .build();

        return req;
    }

}
