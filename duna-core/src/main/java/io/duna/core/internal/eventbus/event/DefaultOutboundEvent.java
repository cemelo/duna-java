package io.duna.core.internal.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.internal.eventbus.LocalEventBus;
import io.duna.core.util.internal;
import org.eclipse.collections.api.multimap.MutableMultimap;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public
@internal
class DefaultOutboundEvent<T> implements OutboundEvent<T> {

    private final LocalEventBus eventBus;
    private final String name;
    private int cost;

    private MutableMultimap<String, String> headers;

    private Predicate<Message<T>> filter;
    private Consumer<Message<T>> interceptor;
    private Consumer<Message<T>> deadLetterSink;

    private T body;

    public DefaultOutboundEvent(LocalEventBus eventBus, String name) {
        this.eventBus = eventBus;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public Predicate<Message<T>> getFilter() {
        return filter;
    }

    @Override
    public Consumer<Message<T>> getInterceptor() {
        return interceptor;
    }

    public T getBody() {
        return this.body;
    }

    @Override
    public OutboundEvent<T> withCost(int cost) {
        this.cost = cost;
        return this;
    }

    @Override
    public OutboundEvent<T> withFilter(Predicate<Message<T>> predicate) {
        this.filter = predicate;
        return this;
    }

    @Override
    public OutboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public OutboundEvent<T> withHeader(String key, String value) {
        Objects.requireNonNull(key, () -> "Header key must be not null.");
        Objects.requireNonNull(value, () -> "Header value must be not null.");

        this.headers.put(key, value);
        return this;
    }

    @Override
    public OutboundEvent<T> withHeader(String key, String... values) {
        Objects.requireNonNull(key, () -> "Header key must be not null.");
        Objects.requireNonNull(values, () -> "Header values must be not null.");

        Arrays.stream(values)
            .filter(Objects::nonNull)
            .forEach(v -> headers.put(key, v));

        return this;
    }

    @Override
    public OutboundEvent<T> withHeader(String key, Iterator<String> values) {
        Objects.requireNonNull(key, () -> "Header key must be not null.");
        Objects.requireNonNull(values, () -> "Header values must be not null.");

        StreamSupport.stream(Spliterators.spliteratorUnknownSize(values, Spliterator.DISTINCT), false)
            .filter(Objects::nonNull)
            .forEach(v -> headers.put(key, v));

        return this;
    }

    @Override
    public OutboundEvent<T> withHeader(Map<String, String> headers) {
        Objects.requireNonNull(headers, () -> "Headers map must be not null.");

        headers.entrySet()
            .stream()
            .filter(Objects::nonNull)
            .filter(e -> e.getKey() != null && e.getValue() != null)
            .forEach(e -> headers.put(e.getKey(), e.getValue()));

        return this;
    }

    @Override
    public OutboundEvent<T> withBody(T body) {
        this.body = body;
        return this;
    }

    @Override
    public OutboundEvent<T> withDeadLetterSink(Consumer<Message<T>> deadLetterConsumer) {
        this.deadLetterSink = deadLetterConsumer;
        return this;
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
