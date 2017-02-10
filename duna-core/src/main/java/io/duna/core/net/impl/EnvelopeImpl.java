package io.duna.core.net.impl;

import io.duna.core.net.Envelope;

import java.util.Collections;
import java.util.Map;

public class EnvelopeImpl<T> implements Envelope<T> {

    private final String source;
    private final String target;
    private final Map<String, String> headers;
    private final T body;

    public EnvelopeImpl(String source, String target, Map<String, String> headers, T body) {
        this.source = source;
        this.target = target;
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
    }

    @Override
    public String source() {
        return null;
    }

    @Override
    public String target() {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public T body() {
        return null;
    }
}
