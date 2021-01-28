package com.bbstone.pisces.client.base.task;

import com.bbstone.pisces.comm.StatusEnum;

public interface ITask {
    public StatusEnum appendFileData(byte[] fileData);
}
