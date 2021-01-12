package com.bbstone.pisces.util;

import io.netty.util.CharsetUtil;
import org.springframework.util.DigestUtils;

public class TestMain {
    public static void main(String[] args) {

        int len = "BBSTONE_BFile".getBytes(CharsetUtil.UTF_8).length;
        System.out.println(len);
        String magic = DigestUtils.md5DigestAsHex("BBSTONE_BFile".getBytes(CharsetUtil.UTF_8));
        System.out.println(magic);



    }
}
