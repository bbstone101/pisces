package com.bbstone.pisces.client.base;

import com.bbstone.pisces.client.base.task.FileTask;
import com.bbstone.pisces.comm.BFileInfo;
import com.bbstone.pisces.proto.BFileMsg;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;

public class ClientCache {

    /** current running Tasks */
    private static Map<String, FileTask> runningTasks = new HashedMap();

    /** client received server filepath list, file data will transferred one by one */
    private static Map<String, BFileInfo> serverFiles = new HashMap<>();

    /** ChunkedFile mode -> key: md5(server_relative_path), value: the BFileRsp info */
    private static Map<String, BFileMsg.BFileRsp> rspInfoMap = new HashMap<>();

    /** ChunkedFile mode -> md5(server_relative_path) */
    private static volatile String recvFileKey = null;

    /**
     * get next transferred filepath and update recvFileKey
     *
     * @return next transferred filepath
     */
    public static BFileInfo nextFile() {
        Iterator<String> it = serverFiles.keySet().iterator();
        if (it.hasNext()) {
            String key = it.next();
            BFileInfo fileInfo = serverFiles.get(key);
            serverFiles.remove(key);
            // md5(server_relative_path)
//            recvFileKey = MD5.asHex(BByteUtil.toBytes(fileInfo.getFilepath()));
            return fileInfo;
        }
        return null;
    }

    /**
     * initial transferring file list
     * <k, v> - <file.checksum, BFileInfo>
     * @param fileList
     */
    public static void init(List<BFileInfo> fileList) {
        serverFiles.clear();
        for (BFileInfo file : fileList) {
//            String key = MD5.asHex(BByteUtil.toBytes(file));
            serverFiles.put(file.getChecksum(), file);
        }
    }

    /**
     *
     * @param id - md5(server_relative_path)
     * @return
     */
    public static FileTask getTask(String id) {
        return runningTasks.get(id);
    }

    /**
     *
     * @param id - md5(server_relative_path)
     * @param task - File Receive & Storage path
     */
    public static void addTask(String id, FileTask task) {
        runningTasks.put(id, task);
    }

    public static void removeTask(String id) {
        runningTasks.remove(id);
    }

    public static String currRecvFileKey() {
        return recvFileKey;
    }

    public static void resetRecvFileKey() {
        recvFileKey = null;
    }


    /**
     * get transferred file BFileRsp info
     *
     * @param key
     * @return
     */
    public static BFileMsg.BFileRsp getRspInfo(String key) {
        return rspInfoMap.get(key);
    }

    public static void addRspInfo(String key, BFileMsg.BFileRsp rsp) {
        recvFileKey = key; // update recvFileKey
        rspInfoMap.put(key, rsp);
    }

    public static void removeRspInfo(String key) {
        recvFileKey = null;
        rspInfoMap.remove(key);
    }



    public static void cleanAll() {
        recvFileKey = null;
        rspInfoMap = Collections.emptyMap();
        serverFiles = Collections.emptyMap();
        runningTasks = Collections.emptyMap();
    }


}
