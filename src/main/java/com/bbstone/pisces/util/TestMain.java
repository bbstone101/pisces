package com.bbstone.pisces.util;

import com.twmacinta.util.MD5;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.util.Arrays;

public class TestMain {
    public static void main(String[] args) {

        int len = "BBSTONE_BFile".getBytes(CharsetUtil.UTF_8).length;
        System.out.println(len);
        String magic = MD5.asHex("BBSTONE_BFile".getBytes(CharsetUtil.UTF_8));
        System.out.println(magic);

        System.out.println(Integer.class.getSimpleName());
        System.out.println(String.class.getSimpleName());
        System.out.println(Long.class.getSimpleName());
        System.out.println(Enum.class.getSimpleName());

        String filepath = "/Users/bbstone/Downloads/test";
//        BFileRequest bFileRequest = new BFileRequest(filepath);
//        System.out.println(bFileRequest.size());
//        System.out.println(BByteUtil.getObjByteSize(bFileRequest));

        System.out.println(Math.round(-1.5));

        String filename = "/Users/bbstone/Downloads/test/Dragon1972.mp4";
        // TODO upgrade to 2.7.1 will suport getHash(File) method
//        String hash = MD5.asHex(MD5.getHash(new File(filename)));

    }

    static int level = -1;
    public static void recursiveReadFileTest() {
        /*try {
            System.out.print("Please input root path:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String path = br.readLine();
            // not input or input a blank
            while ("".equals(path) || " ".equals(path)) {
                System.out.print("Please input root path:");
                path = br.readLine();
            }
            readAllFiles(path);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String path = "/Users/bbstone/Downloads/test/";
        readAllFiles(path);
    }

    /**
     * read all files from a path
     *
     * @param path
     */
    private static void readAllFiles(String path) {
        File f = new File(path);
        level++;
        if (!f.exists()) {
            System.out.println("No such a file or directory!");
            return;
        }
        File[] flist = f.listFiles();
        if (flist == null) {
            System.out.println(f.getName());
            return;
        }
        Arrays.sort(flist);
        for (File ftmp : flist) {

            for (int i = 0; i < level; i++) {
                System.out.print("│  ");
            }

            System.out.println("├── " + ftmp.getName());
            if (ftmp.isDirectory()) {
                readAllFiles(ftmp.getAbsolutePath());
            }
        }
        level--;
    }


}
