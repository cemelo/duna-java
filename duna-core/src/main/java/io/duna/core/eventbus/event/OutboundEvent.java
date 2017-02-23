package io.duna.core.eventbus.event;

import io.duna.core.concurrent.Future;
import io.duna.core.eventbus.Message;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface OutboundEvent2<T> extends Event<Message<T>> {

    <V> Subscriber<V> send();

    <V> void send(Subscriber<V> responseEvent);

    Future<Void> emit();

    Future<Void> publish();

    @Override
    Emitter<T> setFilter(Predicate<Message<T>> predicate);

    @Override
    Emitter<T> setInterceptor(Consumer<Message<T>> interceptor);

    Emitter<T> setHeader(String key, String value);

    Emitter<T> setHeader(String key, String... values);

    Emitter<T> setHeader(String key, Iterator<String> values);

    Emitter<T> setHeader(Map<String, String> headers);

    Emitter<T> setBody(T body);

    Emitter<T> setDeadLetterSink(Consumer<Message<T>> deadLetterConsumer);
}
