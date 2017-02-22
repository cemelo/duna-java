package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface OutboundEvent<T> extends Event<Message<T>> {

    <V> InboundEvent<V> send();

    <V> void send(InboundEvent<V> responseEvent);

    Future<Void> emit();

    Future<Void> publish();

    @Override
    OutboundEvent<T> setFilter(Predicate<Message<T>> predicate);

    @Override
    OutboundEvent<T> setInterceptor(Consumer<Message<T>> interceptor);

    OutboundEvent<T> setHeader(String key, String value);

    OutboundEvent<T> setHeader(String key, String... values);

    OutboundEvent<T> setHeader(String key, Iterator<String> values);

    OutboundEvent<T> setHeader(Map<String, String> headers);

    OutboundEvent<T> setBody(T body);

    OutboundEvent<T> setDeadLetterSink(Consumer<Message<T>> deadLetterConsumer);
}
