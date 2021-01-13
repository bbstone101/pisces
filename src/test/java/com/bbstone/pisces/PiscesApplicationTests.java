package com.bbstone.pisces;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

@SpringBootTest
class PiscesApplicationTests {

    @Test
    void contextLoads() {
    }

    int level = -1;
    @Test
    public void recursiveReadFileTest() {
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
        String path = "/Users/liguifa/Downloads/test/";
        readAllFiles(path);
    }

    /**
     * read all files from a path
     *
     * @param path
     */
    private void readAllFiles(String path) {
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
