package io.duna.core.net.codec;

import io.duna.core.net.impl.EnvelopeImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.eclipse.collections.impl.factory.Multimaps;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EnvelopeCodecsTest {

    private static final byte[] message = new byte[]{
        (byte) 0, 0, 0, 0x0A,     // Size
        (byte) 0xA1, 0x73,        // Source
        (byte) 0xA1, 0x74,        // Target
        (byte) 0x81,              // Header map size
        (byte) 0xA1, 0x68,        // Map key
        (byte) 0xA2, 0x68, 0x76,  // Map value
        (byte) 0x62,              // Body
    };

    private static final EnvelopeImpl<ByteBuf> envelope = new EnvelopeImpl<>("s", "t",
        Multimaps.immutable.bag.with("h", "hv"),
        Unpooled.wrappedBuffer("b".getBytes()));

    @Test
    public void testEncodingValidMessage() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new EnvelopeEncoder());
        channel.writeOutbound(envelope);

        ByteBuf buffer = channel.readOutbound();

        byte[] result = new byte[buffer.readableBytes()];
        buffer.getBytes(0, result);

        assertArrayEquals(message, result);
    }

    @Test
    public void testDecodingValidMessage() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new EnvelopeDecoder());
        channel.writeInbound(Unpooled.wrappedBuffer(message));

        EnvelopeImpl<ByteBuf> received = channel.readInbound();

        assertEquals(envelope, received);
    }
}
