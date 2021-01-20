package com.bbstone.pisces.client.storage;

import com.bbstone.pisces.util.BByteUtil;
import com.twmacinta.util.MD5;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClientCache {

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


}
