package io.duna.core.net.codec;

import io.duna.core.net.impl.envelope.RawEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.util.List;

public class EnvelopeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        int headerLength = in.readInt();

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new ByteBufInputStream(in));

        String source = unpacker.unpackString();
        String target = unpacker.unpackString();

        int headerSize = unpacker.unpackMapHeader();
        MutableMap<String, String> headers = Maps.mutable.ofInitialCapacity(headerSize);
        for (int i = 0; i < headerSize; i++) {
            headers.put(unpacker.unpackString(), unpacker.unpackString());
        }

        in.resetReaderIndex()
            .readerIndex(in.readerIndex() + headerLength);

        ByteBuf body = null;
        if (in.readableBytes() > 0) {
            body = Unpooled.directBuffer(in.readableBytes());
            in.getBytes(in.readerIndex(), body);
        }

        in.readerIndex(in.writerIndex());

        out.add(new RawEnvelope(source, target, headers.toImmutable(), body));
    }
}
