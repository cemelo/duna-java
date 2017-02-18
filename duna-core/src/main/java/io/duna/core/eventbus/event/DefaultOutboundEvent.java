package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.duna.core.function.Consumer;

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
    public OutboundEvent<T> withFilter(Predicate<? super Message<T>> predicate) {
        return null;
    }

    @Override
    public OutboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor) {
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
    public <V> InboundEvent<Message<V>> send() {
        return null;
    }

    @Override
    public <V> void send(Consumer<Message<V>> responseConsumer) {

    }

    @Override
    public Future<Void> emit() {
        return null;
    }

    @Override
    public Future<Void> publish() {
        return null;
    }

    @Override
    public Future<Void> enqueue() {
        return null;
    }
}
