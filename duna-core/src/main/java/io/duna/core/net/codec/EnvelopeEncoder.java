package io.duna.core.net.codec;

import io.duna.core.net.impl.EnvelopeImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.eclipse.collections.api.tuple.Pair;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

public class EnvelopeEncoder extends MessageToByteEncoder<EnvelopeImpl<ByteBuf>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, EnvelopeImpl<ByteBuf> msg, ByteBuf out) throws Exception {
        ByteBufOutputStream outputStream = new ByteBufOutputStream(ctx.alloc()
            .directBuffer(50));

        MessagePacker packer = MessagePack.newDefaultPacker(outputStream);
        packer.packString(msg.source());
        packer.packString(msg.target());
        packer.packMapHeader(msg.headers().size());

        for (Pair<String, String> pair : msg.headers().keyValuePairsView()) {
            packer.packString(pair.getOne());
            packer.packString(pair.getTwo());
        }

        packer.flush();

        out.writeInt(outputStream.writtenBytes());
        out.writeBytes(outputStream.buffer());

        if (msg.body() != null) {
            msg.body().markReaderIndex();
            out.writeBytes(msg.body());
            msg.body().resetReaderIndex();
        }
    }
}
