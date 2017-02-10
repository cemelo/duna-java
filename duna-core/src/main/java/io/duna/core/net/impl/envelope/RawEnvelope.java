package io.duna.core.net.impl.envelope;

import io.duna.core.net.Envelope;
import io.netty.buffer.ByteBuf;
import org.eclipse.collections.api.map.ImmutableMap;

import java.util.Objects;

public class RawEnvelope implements Envelope<ByteBuf> {

    private String source;
    private String target;
    private ImmutableMap<String, String> headers;
    private ByteBuf body;

    public RawEnvelope(String source, String target, ImmutableMap<String, String> headers, ByteBuf body) {
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
    public ImmutableMap<String, String> headers() {
        return headers;
    }

    @Override
    public ByteBuf body() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawEnvelope envelope = (RawEnvelope) o;
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
        return "RawEnvelope{" +
            "source='" + source + '\'' +
            ", target='" + target + '\'' +
            ", headers=" + headers +
            ", body=" + body +
            '}';
    }
}
