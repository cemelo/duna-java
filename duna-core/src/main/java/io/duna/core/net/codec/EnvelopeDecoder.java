package io.duna.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class EnvelopeDecoder extends LengthFieldBasedFrameDecoder {

    public EnvelopeDecoder() {
        super(Integer.MAX_VALUE, 1, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) return null;

        // - source
        // - target
        // - headers
        // - parameters

        return super.decode(ctx, in);
    }
}
