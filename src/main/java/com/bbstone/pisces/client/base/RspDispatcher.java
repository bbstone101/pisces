package com.bbstone.pisces.client.base;

import com.bbstone.pisces.client.cmd.CmdHandler;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.util.BByteUtil;
import com.bbstone.pisces.util.BFileUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.twmacinta.util.MD5;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * server response will dispatch to different client cmdHandler according stream prefix and cmd
 */
@Slf4j
public class RspDispatcher {

    public static void dispatch(ChannelHandlerContext ctx, ByteBuf msg) {
        BFileMsg.BFileRsp rsp = parseFileInfo(msg);
        String cmd = rsp.getCmd();
        CmdHandler cmdHandler = ClientCmdRegister.getHandler(cmd);
        if (cmdHandler == null) {
            log.error("not found cmdHandler for command: {}, please register first.", cmd);
            return;
        }
        cmdHandler.handleNext(ctx, rsp, msg);
    }

    /**
     * Parse BFile Info from msg
     *
     * @param msg
     * @return
     */
    private static BFileMsg.BFileRsp parseFileInfo(ByteBuf msg) {
        BFileMsg.BFileRsp rsp = null;
        // --------------- decode BFileRsp & chunkData
        boolean isBFile = BFileUtil.isBFileStream(msg);
        if (isBFile) {
            // decode bfile_info_len
            int bfileInfoSize = msg.readInt();
            // decode bfile_info
            byte[] bfileInfoData = new byte[bfileInfoSize];
            msg.readBytes(bfileInfoData);
            try {
                rsp = BFileMsg.BFileRsp.parseFrom(bfileInfoData);
                if (rsp == null) {
                    throw new RuntimeException("client recv stream code to bfile error.");
                }
            } catch (InvalidProtocolBufferException e) {
                log.error("parse BFileRs from stream error.", e);
                throw new RuntimeException("parse BFileRs from stream error.");
            }
        } else {
            throw new RuntimeException("recv data is not BFile format.");
        }
        return rsp;
    }

}
