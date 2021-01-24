package com.bbstone.pisces.client.base;

import com.bbstone.pisces.client.task.FileTask;
import com.bbstone.pisces.util.BByteUtil;
import com.twmacinta.util.MD5;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClientCache {

    private static Map<String, FileTask> runningTasks = new HashedMap();
    private static Map<String, String> serverFiles = new HashMap<>();

    public static String nextFile() {
        Iterator<String> it = serverFiles.keySet().iterator();
        if (it.hasNext()) {
            String key = it.next();
            String file = serverFiles.get(key);
            serverFiles.remove(key);
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




}
