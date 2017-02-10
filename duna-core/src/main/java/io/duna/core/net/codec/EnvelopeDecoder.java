package io.duna.core.net.codec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.duna.core.net.Envelope;
import io.duna.core.net.impl.EnvelopeImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.DataInput;
import java.nio.charset.Charset;

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

        CharSequence source = frame.readCharSequence(128, Charset.forName("UTF-8"));
        CharSequence target = frame.readCharSequence(128, Charset.forName("UTF-8"));



        Envelope<ByteBuf> envelope = new EnvelopeImpl<>();


        return super.decode(ctx, in);
    }
}
