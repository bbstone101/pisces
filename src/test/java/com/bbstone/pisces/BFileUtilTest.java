package com.bbstone.pisces;

import com.alibaba.fastjson.JSON;
import com.bbstone.pisces.comm.BFileInfo;
import com.bbstone.pisces.comm.BFileTreeNode;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.BFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BFileUtilTest {

    public static void main(String[] args) {
        /*
        String filepath = "/Users/bbstone/Downloads/test";
        String files = list(filepath);
        log.debug(LF + filepath + LF + files);


        String srvpath = "/Users/bbstone/Downloads/test/cuizhu.jpg";
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

        */

//        String clientdir = getClientDir();
//        System.out.println("clientdir: " + clientdir);

//        String filepath = "/Users/bbstone/Downloads/soft/themestudio-win-11.0.0.100.zip";
//        String filepath = "/Users/bbstone/Downloads/soft";
//        String filepath = "/Users/bbstone/temp-cli/aa";
//        String filepath = "/Users/bbstone/temp-cli/themestudio-win-11.0.0.100.zip";
//        String clipath = BFileUtil.getCliFilepath(filepath);
//        System.out.println("clipath: " + clipath);
//        String tmpfile = BFileUtil.getCliTempFilepath(clipath);
//        System.out.println("tmpfile: " + tmpfile);
/*

        String rpath = getServerRelativePath(filepath);
        log.debug("relative path: {}", rpath);

//        String filepath = rsp.getFilepath();
        String clipath = BFileUtil.getCliFilepath(rpath);
        String temppath = BFileUtil.getCliTempFilepath(clipath);
        log.debug("filepath: {}, clipath: {}, temppath: {}", rpath, clipath, temppath);
*/

//        List<String> fileList = findFiles(getServerDir());
//        for (String file : fileList) {
//            System.out.println(file);
//        }

        byte[] ba = new byte[1024];
        byte[] sbytes = BByteUtil.toBytes("abc");
        System.arraycopy(sbytes, 0, ba, 0, sbytes.length);
        log.debug("sbyte.len: {}, ba.len: {}", sbytes.length, ba.length);

//        byte[] xbytes = BByteUtil.toBytes("xyz");
//        System.arraycopy(xbytes, 0, ba, ba.length, xbytes.length);
//        log.debug("xbyte.len: {}, ba.len: {}", xbytes.length, ba.length);
//        System.out.println(BByteUtil.toStr(ba));

//        System.out.println(12 % 8);
//        System.out.println(System.getProperty("os.name"));
//        String path = "D:\\aa\\bb\\c.txt";
//        String path = "D:/aa/bb/c.txt";
//        System.out.println(BFileUtil.convertToLocalePath(path));

//        List<BFileInfo >fileInfoList = BFileUtil.findServerFiles("/Users/bbstone/Downloads/pisces");
//        for (BFileInfo fileInfo : fileInfoList) {
//            System.out.println(JSON.toJSONString(fileInfo));
//        }
        BFileTreeNode root = BFileUtil.findServerFileTree("/Users/bbstone/Downloads/pisces");
        traversalNode(root);

    }

    public static void traversalNode(BFileTreeNode node) {
        log.info("abspath: {}, name: {}, fileCat: {}", node.getAbsolutePath(), node.getName(), node.getFileCat());
        for (BFileTreeNode n : node.getChildren()) {
            traversalNode(n);
        }
    }

}
