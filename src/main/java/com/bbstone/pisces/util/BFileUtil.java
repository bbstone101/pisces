package com.bbstone.pisces.util;

import com.bbstone.pisces.comm.BFileCmd;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.google.protobuf.ByteString;
import com.twmacinta.util.MD5;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

@Slf4j
public class BFileUtil {

    public static final String LF = "\n";

    private static int level = -1;

    public static boolean isBFileStream(ByteBuf msg) {
        int len = msg.readableBytes();
        log.debug("msg.len: {}", len);

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

    /**
     * return all sub-files relatived to server.dir
     *
     * @param filepath
     * @return
     */
    public static List<String> findFiles(String filepath) {
        if (Files.notExists(Paths.get(filepath))) {
            log.warn("not found file/directory: {}", filepath);
        }
        List<String> fileList = new ArrayList<>();
        File file = new File(filepath);
        if (Files.isDirectory(Paths.get(filepath))) {
            level = -1; // reset
            findFile(fileList, new File(filepath));
        } else {
            fileList.add(getServerRelativePath(file.getAbsolutePath()));
        }
        return fileList;
    }

    private static void findFile(List<String> fileList, File file) {
        File[] flist = file.listFiles();
        Arrays.sort(flist);
        for (File subfile : flist) {
            fileList.add(getServerRelativePath(subfile.getAbsolutePath()));
            if (subfile.isDirectory()) {
                findFile(fileList, subfile);
            }
        }
    }
    // --------------------------

    public static String checksum(File file) {
//        long startTime = System.nanoTime();
        try {
            return MD5.asHex(MD5.getHash(file));
//            return DigestUtils.md5DigestAsHex(new FileInputStream(filepath));
        } catch (IOException e) {
            log.error("calc file hash fail.", e);
//            throw new RuntimeException("calc file hash fail.", e);
        }
        return null;
    }


    public static String checksum(byte[] bytes) {
        return MD5.asHex(bytes);
    }

    /**
     * @param relativePath - server relative path(base on: server.dir)
     * @param check        - check path or not
     * @return
     */
    public static String getClientFullPathWithCheck(String relativePath, boolean check) {
//        String relativePath = getCanonicalPath(filepath).substring(getServerDir().length());
        String clipath = getClientDir() + getCanonicalRelativePath(relativePath);
        String clientpath = convertToLocalePath(clipath);
        if (check)
            checkPath(clientpath);
        return clientpath;
    }

    /**
     * convert path which a server os path to client os path
     * e.g. server is *nix, client is windows,
     * need to convert server path format to client os platform format
     *
     * @param path
     * @return
     */
    public static String convertToLocalePath(String path) {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return path.replaceAll(ConstUtil.NIX_FILE_SEPARATOR, Matcher.quoteReplacement(File.separator));
        } else {
            return path.replaceAll(ConstUtil.WIN_FILE_SEPARATOR, File.separator);
        }
    }

    /**
     * @param serverFullPath
     * @return
     */
    public static String getServerRelativePath(String serverFullPath) {
        if (Files.notExists(Paths.get(serverFullPath)) || !serverFullPath.startsWith(getServerDir())) {
            throw new RuntimeException(String.format("@param(filepath: %s) should be a exists server path and started with(%s).", serverFullPath, getServerDir()));
        }
        return getCanonicalRelativePath(serverFullPath.substring(getServerDir().length()));
    }

//    public static String getClientFullPath(String serverRelativePath) {
//        return getClientDir() + getCanonicalRelativePath(serverRelativePath);
//    }

    public static String getClientDir() {
        String clientdir = Config.clientDir;
        if (StringUtils.isBlank(clientdir)) {
            throw new RuntimeException("client.dir property cannot be empty.");
        }
        return getCanonicalPath(clientdir);
    }

    public static String getServerDir() {
        String serverdir = Config.serverDir;
        if (StringUtils.isBlank(serverdir)) {
            throw new RuntimeException("server.dir property cannot be empty.");
        }
        return getCanonicalPath(serverdir);
    }

    /**
     * if path not end with File.separator, append to it before return
     *
     * @param path
     * @return
     */
    private static String getCanonicalPath(String path) {
        // append last File.separator for dir
        if (Files.isDirectory(Paths.get(path))) {
            path = (path.lastIndexOf(File.separator) == (path.length() - 1)) ? path : path + File.separator;
        }
        return path;
    }

    /**
     * remove the first File.separator
     *
     * @param path
     * @return
     */
    private static String getCanonicalRelativePath(String path) {
        path = convertToLocalePath(path);
        // remove the first File.separator
        if (path.startsWith(File.separator)) {
            path = path.substring(File.separator.length());
        }
        return path;
    }

    /**
     * @param relativePath
     * @return
     */
    public static String getClientFullPathWithCheck(String relativePath) {
        return getClientFullPathWithCheck(relativePath, Boolean.TRUE);
    }

    private static void checkPath(String clipath) {
        String dirpath = getCanonicalPath(clipath).substring(0, clipath.lastIndexOf(File.separator));
        if (Files.notExists(Paths.get(dirpath))) {
            mkdir(dirpath);
        }
        log.debug("checkPath-> dirpath: {}", dirpath);
//        if (Files.isDirectory(Paths.get(clipath))) {
//            dirpath = clipath;
//        } else {
////            dirpath = clipath.substring(0, clipath.lastIndexOf(System.getProperty("file.separator")));
//            dirpath = clipath.substring(0, clipath.lastIndexOf(File.separator));
//        }
    }

    /**
     * create directory
     *
     * @param dirpath -  a directory path
     */
    public static void mkdir(String dirpath) {
        if (Files.notExists(Paths.get(dirpath))) {
            try {
                Files.createDirectories(Paths.get(dirpath));
                log.debug(">>>>>>>>>>>>>> created dir: {}", dirpath);
            } catch (IOException e) {
                log.error("dir create fail. ", e);
            }
        }
    }

    /**
     * @param clientFullPath - client full path
     * @return
     */
    public static String getClientTempFileFullPath(String clientFullPath) {
        if (isDir(clientFullPath)) return clientFullPath;
        return clientFullPath + Config.tempFilePostfix;
    }

    /**
     * @param serverRelativeFile
     * @return
     */
    public static String getPartFileFromRelative(String serverRelativeFile) {
        String clientFullPath = BFileUtil.getClientFullPathWithCheck(serverRelativeFile);
        String tempFile = BFileUtil.getClientTempFileFullPath(clientFullPath);
        return tempFile;
    }

    /**
     * @param serverFileFullPath
     * @return
     */
    public static String getPartFileFromFull(String serverFileFullPath) {
        String serverRelativeFile = getServerRelativePath(serverFileFullPath);
        String clientFullPath = BFileUtil.getClientFullPathWithCheck(serverRelativeFile);
        String tempFile = BFileUtil.getClientTempFileFullPath(clientFullPath);
        return tempFile;
    }


    public static boolean isDir(String filepath) {
        return Files.isDirectory(Paths.get(filepath));
    }

    /**
     * rename temp file to client file
     *
     * @param tmpFile
     * @param clipath
     */
    public static void renameCliTempFile(File tmpFile, String clipath) {
        tmpFile.renameTo(new File(clipath));
    }

    /**
     * build FileRsp
     *
     * @param serverpath
     * @param filesize
     * @param checksum
     * @param reqTs
     * @return
     */
    public static ByteBuf buildRspFile(String serverpath, long filesize, String checksum, long reqTs) {
        return buildRsp(BFileCmd.RSP_FILE, serverpath, filesize, checksum, null, null, reqTs);
    }

    public static ByteBuf buildRspDir(String serverpath, long reqTs) {
        return buildRsp(BFileCmd.RSP_DIR, serverpath, 0, ConstUtil.EMPTY_STR, null, null, reqTs);
    }

    /**
     * build ListRsp
     *
     * @param serverpath
     * @param rspData
     * @param reqTs
     * @return
     */
    public static ByteBuf buildRspList(String serverpath, String rspData, long reqTs) {
        // checksum: rspData 's md5 hash
        String checksum = MD5.asHex(BByteUtil.toBytes(rspData));
        return buildRsp(BFileCmd.RSP_LIST, serverpath, 0, checksum, rspData, null, reqTs);
    }


    /**
     * BFileRsp intro:
     * it contains 2 field for store server response data(rspData/chunkData),
     * usage:
     * rspData - string, can be json string or only some string words
     * fileChunkData - byte[], byte array data
     * <p>
     * <p>
     * Standard Rsp format like:
     * +--------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | delimiter |
     * +--------------------------------------------------------------------+
     * <p>
     * <p>
     * Non-standard format(append byte[] data after Rsp object, because cannot retrieve the data and set to Rsp.chunkData:
     * e.g. FileRsp(cmd: CMD_REQ) data format(only for FileRegion which directly write file data to channel):
     * +---------------------------------------------------------------------------------+
     * | bfile_info_prefix | bfile_info_bytes(int) | bfile_info | chunk_data | delimiter |
     * +---------------------------------------------------------------------------------+
     * <p>
     * bfile_info_prefix:
     * <p>
     * Note: delimiter not encode in upper format, need append delimiter after
     * this method invoked, see "bytes size format" in method body
     *
     * @param cmd
     * @param serverpath
     * @param filesize
     * @param checksum
     * @param rspData
     * @param chunkData
     * @param reqTs
     * @return
     */
    private static ByteBuf buildRsp(String cmd, String serverpath, long filesize, String checksum, String rspData, byte[] chunkData, long reqTs) {
        // BFile info prefix
        byte[] prefix = BByteUtil.toBytes(ConstUtil.bfile_info_prefix);
        // BFile info
        String filepath = getServerRelativePath(serverpath);
        BFileMsg.BFileRsp rsp = BFileMsg.BFileRsp.newBuilder()
                .setId(MD5.asHex(BByteUtil.toBytes(filepath)))
                .setCmd(cmd) //BFileCmd.CMD_RSP)
                .setFilepath(filepath) // relative path(not contains client.dir or server.dir)
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

        log.debug("data.len: {}, bytes: {}", data.length, data.toString());
        return Unpooled.wrappedBuffer(data);
    }


    /**
     * @param cmd
     * @param filepath
     * @return
     */
    public static BFileMsg.BFileReq buildReq(String cmd, String filepath) {
        BFileMsg.BFileReq req = BFileMsg.BFileReq.newBuilder()
                .setId(MD5.asHex(BByteUtil.toBytes(filepath)))
                .setCmd(cmd)
                .setFilepath(filepath)
                .setTs(System.currentTimeMillis())
                .build();

        return req;
    }




}
