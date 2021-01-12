package com.bbstone.pisces.proto.codec;

import com.bbstone.pisces.proto.model.BFileBase;
import io.netty.buffer.ByteBuf;

public interface BFileCodec {

    public BFileBase decode(ByteBuf msg);

    public ByteBuf encode(BFileBase bFileBase);
}
