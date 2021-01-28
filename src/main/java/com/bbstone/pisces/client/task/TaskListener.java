package com.bbstone.pisces.client.task;

import com.bbstone.pisces.proto.BFileMsg;

public interface TaskListener {
    public void onCompleted(BFileMsg.BFileRsp rsp);
}
