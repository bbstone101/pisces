package com.bbstone.pisces.util;

import com.bbstone.pisces.client.storage.ClientCache;
import com.bbstone.pisces.comm.BFileCmd;
import com.bbstone.pisces.proto.BFileMsg;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CtxUtil {

    public static String reqNextFile(ChannelHandlerContext ctx) {
        String nextFile = ClientCache.nextFile();
        if (StringUtils.isNotBlank(nextFile)) {
//            String serverRelativeFile = BFileUtil.getServerRelativePath(nextFile);
//            log.debug("after list files, req to download nextFile: {}, serverRelativeFile: {}", nextFile, serverRelativeFile);
            log.debug("after list files, req to download nextFile: {}", nextFile);
            // FileReq
            BFileMsg.BFileReq req = BFileUtil.buildReq(BFileCmd.REQ_FILE, nextFile);
            ctx.write(req);
            ctx.writeAndFlush(Unpooled.wrappedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8)));
            return nextFile;
        }
        return null;
    }


}
