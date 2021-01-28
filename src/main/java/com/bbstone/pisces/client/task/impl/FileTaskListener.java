package com.bbstone.pisces.client.task.impl;

import com.bbstone.pisces.client.base.ClientCache;
import com.bbstone.pisces.client.task.TaskListener;
import com.bbstone.pisces.proto.BFileMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FileTaskListener implements TaskListener {

    /**
     * reset recvFileKey to null,
     * clean ClientCache RspInfo with the recvFileKey
     * clean running task with the recvFileKey
     *
     * @param rsp - file response info(BFileRsp)
     */
    @Override
    public void onCompleted(BFileMsg.BFileRsp rsp) {
        String recvFileKey = ClientCache.currRecvFileKey();
        if (StringUtils.isNotBlank(recvFileKey)) {
            ClientCache.removeRspInfo(recvFileKey);
        }
        ClientCache.resetRecvFileKey();
        ClientCache.removeTask(rsp.getId());
    }
}
