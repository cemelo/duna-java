package io.duna.core.net.codec;

import io.duna.core.net.impl.envelope.BufferEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.util.List;

public class EnvelopeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(in);
        in.markReaderIndex();

        int envelopeHeaderLength = in.readInt();
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new ByteBufInputStream(in));

        System.out.println(envelopeHeaderLength);

        String source = unpacker.unpackString();
        String target = unpacker.unpackString();

        int headersMapSize = unpacker.unpackMapHeader();
        MutableMultimap<String, String> headers = Multimaps.mutable.bag.empty();
        for (int i = 0; i < headersMapSize; i++) {
            headers.put(unpacker.unpackString(), unpacker.unpackString());
        }

        in.resetReaderIndex()
            .readerIndex(in.readerIndex() + envelopeHeaderLength);

        ByteBuf body;
        if (in.readableBytes() > 0) {
            body = Unpooled.buffer(in.readableBytes());
            in.getBytes(in.readerIndex(), body);
        } else {
            body = Unpooled.wrappedBuffer(new byte[] {});
        }

        in.readerIndex(in.writerIndex());

        out.add(new BufferEnvelope(source, target, headers.toImmutable(), body.nioBuffer()));
    }
}
