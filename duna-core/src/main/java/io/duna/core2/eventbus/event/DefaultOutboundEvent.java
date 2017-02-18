package io.duna.core2.eventbus.event;

import io.duna.core2.concurrent.future.Future;
import io.duna.core2.eventbus.Message;
import io.duna.core2.function.Consumer;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

public class DefaultOutboundEvent<T> implements OutboundEvent<T> {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public OutboundEvent<T> withCost(int cost) {
        return null;
    }

    @Override
    public OutboundEvent<T> withFilter(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public OutboundEvent<T> withInterceptor(Consumer<T> interceptorConsumer) {
        return null;
    }

    @Override
    public OutboundEvent<T> withHeader(String key, String value) {
        return null;
    }

    @Override
    public OutboundEvent<T> withHeader(String key, String... values) {
        return null;
    }

    @Override
    public OutboundEvent<T> withHeader(String key, Iterator<String> values) {
        return null;
    }

    @Override
    public OutboundEvent<T> withHeader(Map<String, String> headers) {
        return null;
    }

    @Override
    public OutboundEvent<T> withBody(T body) {
        return null;
    }

    @Override
    public OutboundEvent<T> withDeadLetterSink(Consumer<Message<T>> deadLetterConsumer) {
        return null;
    }

    @Override
    public <V> Future<Message<V>> emit() {
        return null;
    }

    @Override
    public <V> void emit(Consumer<Message<V>> responseConsumer) {

    }

    @Override
    public void publish() {

    }

    @Override
    public void enqueue() {

    }
}
