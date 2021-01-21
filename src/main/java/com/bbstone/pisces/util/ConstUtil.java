package com.bbstone.pisces.util;

import io.netty.util.CharsetUtil;

public class ConstUtil {

    public static final String EMPTY_STR = "";
    public static final String magic = "BBSTONE_BFile";
    public static final int magicLen = magic.getBytes(CharsetUtil.UTF_8).length;

    // cannot used ":-)" as delimiter, when send video will cause lost bytes
    public static final String bfile_info_prefix = "__10BBSTONE_BFILE_START01__";
    public static final int bfile_info_prefix_len = bfile_info_prefix.getBytes(CharsetUtil.UTF_8).length;;

    public static final String delimiter = "__10BBSTONE_BFILE_END01__";


    public static final int ACK_OK = 0;
    public static final int ACK_FAIL = 1;

    /** default chunk size is 8k */
    public static final int DEFAULT_CHUNK_SIZE = 1024 * 8; // 8k


    /** regex in String.replaceAll() method */
    public static final String WIN_FILE_SEPARATOR = "\\\\";
    public static final String NIX_FILE_SEPARATOR = "\\/";



    public static final String TYPE_INTEGER = Integer.class.getSimpleName(); // return Integer
    public static final String TYPE_LONG = Long.class.getSimpleName(); // return Long
    public static final String TYPE_STRING = String.class.getSimpleName(); // return String







}
