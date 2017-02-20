package io.duna.core.internal.eventbus;

import io.duna.core.eventbus.Message;
import org.eclipse.collections.api.multimap.Multimap;

public final class SimpleMessage<T> implements Message<T> {

    private String source;
    private String target;
    private String responseEvent;
    private Multimap<String, String> headers;
    private T body;
    private Throwable cause;

    public SimpleMessage(String source, String target, String responseEvent,
                         Multimap<String, String> headers, T body, Throwable cause) {
        this.source = source;
        this.target = target;
        this.responseEvent = responseEvent;
        this.headers = headers;
        this.body = body;
        this.cause = cause;
    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public String getTarget() {
        return null;
    }

    @Override
    public String getResponseEvent() {
        return null;
    }

    @Override
    public Multimap<String, String> getHeaders() {
        return null;
    }

    @Override
    public T getBody() {
        return null;
    }

    @Override
    public Throwable getCause() {
        return null;
    }

    @Override
    public boolean succeeded() {
        return !failed();
    }

    @Override
    public boolean failed() {
        return cause != null;
    }

    @Override
    public <V> void reply(V response) {

    }

    @Override
    public void fail(Throwable t) {

    }
}
