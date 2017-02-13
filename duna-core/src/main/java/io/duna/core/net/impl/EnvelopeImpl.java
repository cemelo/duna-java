package io.duna.core.net.impl;

import io.duna.core.net.Envelope;
import org.eclipse.collections.api.multimap.ImmutableMultimap;

import java.util.Objects;

public class EnvelopeImpl<T> implements Envelope<T> {

    private String source;
    private String target;
    private ImmutableMultimap<String, String> headers;
    private T body;

    public EnvelopeImpl(String source,
                        String target,
                        ImmutableMultimap<String, String> headers,
                        T body) {
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
    public T body() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvelopeImpl envelope = (EnvelopeImpl) o;
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
        return "EnvelopeImpl{" +
            "source='" + source + '\'' +
            ", target='" + target + '\'' +
            ", headers=" + headers +
            ", body=" + body +
            '}';
    }
}
