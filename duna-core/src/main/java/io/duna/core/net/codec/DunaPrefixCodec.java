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
        msg.retain();
        ByteBuf result = Unpooled
            .compositeBuffer()
            .addComponents(Unpooled.buffer(1).writeByte(PREFIX), msg)
            .writerIndex(msg.writerIndex() + 1);

        out.add(result);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readUnsignedByte() != PREFIX)
            throw new IllegalStateException("Invalid message.");

        msg.retain();
        out.add(msg);
    }
}
