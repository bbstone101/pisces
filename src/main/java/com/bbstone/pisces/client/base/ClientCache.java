package com.bbstone.pisces.client.base;

import com.bbstone.pisces.client.task.FileTask;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BByteUtil;
import com.twmacinta.util.MD5;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;

public class ClientCache {

    private static Map<String, FileTask> runningTasks = new HashedMap();
    private static Map<String, String> serverFiles = new HashMap<>();

    /** key: md5(recvFile), value: the BFileRsp info */
    private static Map<String, BFileMsg.BFileRsp> rspInfoMap = new HashMap<>();
    private static volatile String recvFileKey = null;

    public static String nextFile() {
        Iterator<String> it = serverFiles.keySet().iterator();
        if (it.hasNext()) {
            String key = it.next();
            String file = serverFiles.get(key);
            serverFiles.remove(key);
            recvFileKey = MD5.asHex(BByteUtil.toBytes(file));
            return file;
        }
        return null;
    }

    public static void init(List<String> fileList) {
        serverFiles.clear();
        for (String file : fileList) {
            String key = MD5.asHex(BByteUtil.toBytes(file));
            serverFiles.put(key, file);
        }
    }

    public static FileTask getTask(String id) {
        return runningTasks.get(id);
    }

    public static void addTask(String id, FileTask task) {
        runningTasks.put(id, task);
    }

    public static void removeTask(String id) {
        runningTasks.remove(id);
    }

    public static String currRecvFileKey() {
        return recvFileKey;
    }


    public static BFileMsg.BFileRsp getRspInfo(String key) {
        return rspInfoMap.get(key);
    }

    public static void addRspInfo(String key, BFileMsg.BFileRsp rsp) {
        rspInfoMap.put(key, rsp);
    }

    public static void removeRspInfo(String key) {
        rspInfoMap.remove(key);
    }



    public static void cleanAll() {
        recvFileKey = null;
        rspInfoMap = Collections.emptyMap();
        serverFiles = Collections.emptyMap();
        runningTasks = Collections.emptyMap();
    }


}
