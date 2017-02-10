package io.duna.core.net.codec;

import io.duna.core.net.Envelope;
import io.duna.core.net.impl.envelope.RawEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EnvelopeCodecsTest {

    @Test
    public void testEncodingDecodingValidMessage() throws Exception {
        RawEnvelope sent = new RawEnvelope("source", "target",
            Maps.immutable.with("some-header", "some header value"), Unpooled.copiedBuffer("body".getBytes()));

        EmbeddedChannel channel = new EmbeddedChannel(new DunaPrefixCodec(),
            new EnvelopeEncoder(),
            new EnvelopeDecoder());

        channel.writeOutbound(sent);
        channel.writeInbound((ByteBuf) channel.readOutbound());

        Envelope<ByteBuf> received = channel.readInbound();

        assertEquals(sent.source(), received.source());
        assertEquals(sent.target(), received.target());
        assertEquals(sent.headers(), received.headers());

        assertNotNull(received.body());
        assertEquals(sent.body().toString(Charset.defaultCharset()),
            received.body().toString(Charset.defaultCharset()));
    }
}
