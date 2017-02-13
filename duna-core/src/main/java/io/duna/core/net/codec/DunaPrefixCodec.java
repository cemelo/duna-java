package io.duna.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class DunaPrefixCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {

    private static final short PREFIX = 0xDF;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(ctx.alloc()
            .buffer(1)
            .writeByte(0xDF));
        out.add(msg.retain());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readUnsignedByte() != PREFIX)
            throw new IllegalStateException("Invalid message.");

        out.add(msg.retainedSlice(msg.readerIndex(), msg.readableBytes()));
    }
}
