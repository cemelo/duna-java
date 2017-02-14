package io.duna.core.net.codec;

import io.duna.core.net.Envelope;
import io.duna.core.net.impl.EnvelopeImpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Multimaps;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.util.List;

public class EnvelopeCodec extends ByteToMessageCodec<Envelope<ByteBuf>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Envelope<ByteBuf> msg, ByteBuf out)
    throws Exception {
        ByteBufOutputStream outputStream = new ByteBufOutputStream(out);

        MessagePacker packer = MessagePack.newDefaultPacker(outputStream);
        packer.packString(msg.source());
        packer.packString(msg.target());
        packer.packMapHeader(msg.headers().size());

        for (Pair<String, String> pair : msg.headers().keyValuePairsView()) {
            if (pair.getOne() == null || pair.getTwo() == null)
                throw new IllegalArgumentException("Header key or value cannot be null.");

            packer.packString(pair.getOne());
            packer.packString(pair.getTwo());
        }

        packer.packNil();
        packer.flush();

        if (msg.body() != null) {
            msg.body().markReaderIndex();
            outputStream.buffer().writeBytes(msg.body());
            msg.body().resetReaderIndex();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
    throws Exception {
        int endOfHeader = in.forEachByte(b -> Byte.toUnsignedInt(b) != 0xC0);

        ByteBufInputStream inputStream = new ByteBufInputStream(in);
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(inputStream);

        String source = unpacker.unpackString();
        String target = unpacker.unpackString();

        int headersMapSize = unpacker.unpackMapHeader();
        MutableMultimap<String, String> headers = Multimaps.mutable.bag.empty();
        for (int i = 0; i < headersMapSize; i++) {
            headers.put(unpacker.unpackString(), unpacker.unpackString());
        }

        in.readerIndex(endOfHeader + 1);

        ByteBuf body;
        if (in.readableBytes() > 0) {
            body = in.readRetainedSlice(in.readableBytes());
        } else {
            body = Unpooled.EMPTY_BUFFER;
        }

        out.add(new EnvelopeImpl<>(source, target, headers.toImmutable(), body));
    }
}
