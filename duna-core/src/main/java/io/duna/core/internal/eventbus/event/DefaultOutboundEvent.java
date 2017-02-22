package io.duna.core.internal.eventbus.event;

import io.duna.core.Context;
import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.internal.eventbus.MultithreadLocalEventBus;
import io.duna.core.internal.eventbus.SimpleMessage;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.impl.factory.Multimaps;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class DefaultOutboundEvent<T> implements OutboundEvent<T> {

    private final MultithreadLocalEventBus eventBus;
    private final String name;

    private MutableMultimap<String, String> headers;

    private Predicate<Message<T>> filter;
    private Consumer<Message<T>> interceptor;
    private Consumer<Message<T>> deadLetterSink;

    private T body;

    public DefaultOutboundEvent(MultithreadLocalEventBus eventBus, String name) {
        this.eventBus = eventBus;
        this.name = name;
        this.headers = Multimaps.mutable.set.empty();
    }

    @Override
    public <V> InboundEvent<V> send() {
        InboundEvent<V> responseEvent = eventBus.inbound(UUID.randomUUID().toString());
        responseEvent.addListener(m -> eventBus.cancel(responseEvent));

        eventBus.register(responseEvent);

        emit(responseEvent.getName(), false);

        return responseEvent;
    }

    @Override
    public <V> void send(InboundEvent<V> responseEvent) {
        emit(responseEvent.getName(), false);
    }

    @Override
    public Future<Void> emit() {
        return emit(null, false);
    }

    @Override
    public Future<Void> publish() {
        return emit(null, true);
    }

    @Override
    public String getName() {
        return name;
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
    public OutboundEvent<T> setFilter(Predicate<Message<T>> predicate) {
        this.filter = predicate;
        return this;
    }

    @Override
    public OutboundEvent<T> setInterceptor(Consumer<Message<T>> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public OutboundEvent<T> setHeader(String key, String value) {
        Objects.requireNonNull(key, () -> "Header key must be not null.");
        Objects.requireNonNull(value, () -> "Header value must be not null.");

        this.headers.put(key, value);
        return this;
    }

    @Override
    public OutboundEvent<T> setHeader(String key, String... values) {
        Objects.requireNonNull(key, () -> "Header key must be not null.");
        Objects.requireNonNull(values, () -> "Header values must be not null.");

        Arrays.stream(values)
            .filter(Objects::nonNull)
            .forEach(v -> headers.put(key, v));

        return this;
    }

    @Override
    public OutboundEvent<T> setHeader(String key, Iterator<String> values) {
        Objects.requireNonNull(key, () -> "Header key must be not null.");
        Objects.requireNonNull(values, () -> "Header values must be not null.");

        StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(values, Spliterator.DISTINCT),
                false)
            .filter(Objects::nonNull)
            .forEach(v -> headers.put(key, v));

        return this;
    }

    @Override
    public OutboundEvent<T> setHeader(Map<String, String> headers) {
        Objects.requireNonNull(headers, () -> "Headers map must be not null.");

        headers.entrySet()
            .stream()
            .filter(Objects::nonNull)
            .filter(e -> e.getKey() != null && e.getValue() != null)
            .forEach(e -> headers.put(e.getKey(), e.getValue()));

        return this;
    }

    @Override
    public OutboundEvent<T> setBody(T body) {
        this.body = body;
        return this;
    }

    @Override
    public OutboundEvent<T> setDeadLetterSink(Consumer<Message<T>> deadLetterConsumer) {
        Objects.requireNonNull(deadLetterConsumer,
            () -> "The dead letter consumer must be not null.");
        this.deadLetterSink = deadLetterConsumer;
        return this;
    }

    @SuppressWarnings("SameParameterValue")
    private Future<Void> emit(String responseEvent, boolean multicast) {
        String sourceEventName = null;

        if (Context.currentContext() != null) {
            Event<?> currentEvent = Context.currentContext().get("current-event");
            sourceEventName = currentEvent != null ? currentEvent.getName() : null;
        }

        Message<T> output = SimpleMessage.<T> builder()
            .source(sourceEventName)
            .target(this.getName())
            .responseEvent(responseEvent)
            .body(this.body)
            .headers(this.headers)
            .parent(this.eventBus)
            .build();

        return eventBus.dispatch(output, multicast, this.deadLetterSink);
    }
}
