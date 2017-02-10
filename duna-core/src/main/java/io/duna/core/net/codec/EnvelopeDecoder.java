package io.duna.core.net.codec;

import io.duna.core.net.Envelope;
import io.duna.core.net.impl.EnvelopeImpl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class EnvelopeDecoder extends LengthFieldBasedFrameDecoder {

    private final ObjectMapper objectMapper;

    public EnvelopeDecoder(ObjectMapper objectMapper) {
        super(Integer.MAX_VALUE,
            1,
            4,
            0,
            5);

        this.objectMapper = objectMapper;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        short magic = in.getShort(0);
        if (magic != 0xDF)
            throw new IllegalStateException("Invalid message.");

        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) return null;

        int headerSize = frame.readInt();

        Envelope<ByteBuf> envelope = new EnvelopeImpl<>();


        return super.decode(ctx, in);
    }
}
