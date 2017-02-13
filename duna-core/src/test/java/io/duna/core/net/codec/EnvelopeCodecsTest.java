package io.duna.core.net.codec;

import io.duna.core.net.Envelope;
import io.duna.core.net.impl.envelope.BufferEnvelope;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.eclipse.collections.impl.factory.Multimaps;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EnvelopeCodecsTest {

    @Test
    public void testEncodingValidMessage() throws Exception {
        BufferEnvelope sent = new BufferEnvelope("s", "t",
            Multimaps.immutable.bag.with("h", "hv"),
            ByteBuffer.wrap("b".getBytes()));

        EmbeddedChannel channel = new EmbeddedChannel(new EnvelopeEncoder());
        channel.writeOutbound(sent);

        ByteBuf buffer = channel.readOutbound();

        byte[] result = new byte[buffer.readableBytes()];
        buffer.getBytes(0, result);

        assertArrayEquals(new byte[] {
            (byte) 0, 0, 0, 0x0A,     // Size
            (byte) 0xA1, 0x73,        // Source
            (byte) 0xA1, 0x74,        // Target
            (byte) 0x81,              // Header map size
            (byte) 0xA1, 0x68,        // Map key
            (byte) 0xA2, 0x68, 0x76,  // Map value
            (byte) 0x62,              // Body
        }, result);
    }
}
