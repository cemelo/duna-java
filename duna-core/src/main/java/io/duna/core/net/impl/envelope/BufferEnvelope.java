package io.duna.core.net.impl.envelope;

import io.duna.core.net.Envelope;

import org.eclipse.collections.api.multimap.ImmutableMultimap;

import java.nio.ByteBuffer;
import java.util.Objects;

public class BufferEnvelope implements Envelope<ByteBuffer> {

    private String source;
    private String target;
    private ImmutableMultimap<String, String> headers;
    private ByteBuffer body;

    public BufferEnvelope(String source,
                          String target,
                          ImmutableMultimap<String, String> headers,
                          ByteBuffer body) {
        this.source = source;
        this.target = target;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public String target() {
        return target;
    }

    @Override
    public ImmutableMultimap<String, String> headers() {
        return headers;
    }

    @Override
    public ByteBuffer body() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BufferEnvelope envelope = (BufferEnvelope) o;
        return Objects.equals(source, envelope.source) &&
            Objects.equals(target, envelope.target) &&
            Objects.equals(headers, envelope.headers) &&
            Objects.equals(body, envelope.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, headers, body);
    }

    @Override
    public String toString() {
        return "BufferEnvelope{" +
            "source='" + source + '\'' +
            ", target='" + target + '\'' +
            ", headers=" + headers +
            ", body=" + body +
            '}';
    }
}
