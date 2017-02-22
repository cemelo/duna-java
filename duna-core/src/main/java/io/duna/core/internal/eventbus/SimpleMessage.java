package io.duna.core.internal.eventbus;

import io.duna.core.eventbus.EventBus;
import io.duna.core.eventbus.Message;
import org.eclipse.collections.api.multimap.Multimap;

public final class SimpleMessage<T> implements Message<T> {

    private EventBus parent;
    private String source;
    private String target;
    private String responseEvent;
    private Multimap<String, String> headers;
    private T body;
    private Throwable cause;

    public SimpleMessage(EventBus parent, String source, String target, String responseEvent,
                         Multimap<String, String> headers, T body, Throwable cause) {
        this.parent = parent;
        this.source = source;
        this.target = target;
        this.responseEvent = responseEvent;
        this.headers = headers;
        this.body = body;
        this.cause = cause;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public String getRespondTo() {
        return responseEvent;
    }

    @Override
    public Multimap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public T getBody() {
        return body;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public boolean succeeded() {
        return !failed();
    }

    @Override
    public boolean failed() {
        return cause != null;
    }

    public static <V> Builder<V> builder() {
        return new Builder<>();
    }

    public static class Builder<V> {
        private EventBus parent;
        private String source;
        private String target;
        private String responseEvent;
        private Multimap<String, String> headers;
        private V body;
        private Throwable cause;

        public Builder<V> parent(EventBus parent) {
            this.parent = parent;
            return this;
        }

        public Builder<V> source(String source) {
            this.source = source;
            return this;
        }

        public Builder<V> target(String target) {
            this.target = target;
            return this;
        }

        public Builder<V> responseEvent(String responseEvent) {
            this.responseEvent = responseEvent;
            return this;
        }

        public Builder<V> headers(Multimap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder<V> body(V body) {
            this.body = body;
            return this;
        }

        public Builder<V> cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public SimpleMessage<V> build() {
            return new SimpleMessage<>(parent, source, target, responseEvent, headers, body, cause);
        }
    }
}
