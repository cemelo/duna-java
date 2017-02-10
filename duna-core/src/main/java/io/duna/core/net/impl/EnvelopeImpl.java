package io.duna.core.net.impl;

import io.duna.core.net.Envelope;

import java.util.Collections;
import java.util.Map;

public class EnvelopeImpl<T> implements Envelope<T> {

    private String source;
    private String target;
    private Map<String, String> headers;
    private T body;

    @Override
    public String source() {
        return source;
    }

    @Override
    public String target() {
        return target;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public T body() {
        return body;
    }
}
