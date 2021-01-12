package com.bbstone.pisces.util;

import io.netty.util.CharsetUtil;

public class ConstUtil {

    public static final String magic = "BBSTONE_BFile";
    public static final int magicLen = magic.getBytes(CharsetUtil.UTF_8).length;

    public static final int ACK_OK = 0;
    public static final int ACK_FAIL = 1;

    /** default chunk size is 8k */
    public static final int DEFAULT_CHUNK_SIZE = 1024 * 8; // 8k


    // cannot used ":-)" as delimiter, when send video will cause lost bytes
    public static final String delimiter = "__BBSTONE_BFILE_END__";
}
