package com.bbstone.pisces.util;

import io.netty.util.CharsetUtil;
import org.springframework.util.DigestUtils;

public class TestMain {
    public static void main(String[] args) {

        int len = "BBSTONE_BFile".getBytes(CharsetUtil.UTF_8).length;
        System.out.println(len);
        String magic = DigestUtils.md5DigestAsHex("BBSTONE_BFile".getBytes(CharsetUtil.UTF_8));
        System.out.println(magic);

        System.out.println(Integer.class.getSimpleName());
        System.out.println(String.class.getSimpleName());
        System.out.println(Long.class.getSimpleName());
        System.out.println(Enum.class.getSimpleName());

        String filepath = "/Users/liguifa/Downloads/test";
//        BFileRequest bFileRequest = new BFileRequest(filepath);
//        System.out.println(bFileRequest.size());
//        System.out.println(BByteUtil.getObjByteSize(bFileRequest));

        System.out.println(Math.round(-1.5));

    }
}
