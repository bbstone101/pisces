package com.bbstone.pisces.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class BFileUtil {

    public static final String LF = "\n";

    private static int level = -1;

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

    public static void main(String[] args) {
        String filepath = "/Users/liguifa/Downloads/test";
        String files = list(filepath);
        log.info(LF + filepath + LF + files);
    }

}
