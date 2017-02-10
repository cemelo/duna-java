package io.duna.core.net.codec;

import io.duna.core.net.impl.envelope.RawEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.eclipse.collections.api.tuple.Pair;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

public class EnvelopeEncoder extends MessageToByteEncoder<RawEnvelope> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RawEnvelope msg, ByteBuf out) throws Exception {
        ByteBufOutputStream outputStream = new ByteBufOutputStream(Unpooled.buffer());

        MessagePacker packer = MessagePack.newDefaultPacker(outputStream);
        packer.packString(msg.source());
        packer.packString(msg.target());
        packer.packMapHeader(msg.headers().size());

        for (Pair<String, String> pair : msg.headers().keyValuesView()) {
            packer.packString(pair.getOne());
            packer.packString(pair.getTwo());
        }

        packer.flush();

        int headerLength = outputStream.buffer().writerIndex();
        out.writeInt(headerLength);

        out.writeBytes(outputStream.buffer());
        if (msg.body() != null) {
            out.writeBytes(msg.body());
            msg.body().resetReaderIndex();
        }
    }
}
