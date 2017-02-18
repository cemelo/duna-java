package io.duna.core2.eventbus.event;

import io.duna.core2.concurrent.future.Future;
import io.duna.core2.eventbus.Message;
import io.duna.core2.function.Consumer;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

public interface OutboundEvent<T> extends Event<Message<T>> {

    @Override
    OutboundEvent<T> withCost(int cost);

    @Override
    OutboundEvent<T> withFilter(Predicate<? super Message<T>> predicate);

    @Override
    OutboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor);

    OutboundEvent<T> withHeader(String key, String value);

    OutboundEvent<T> withHeader(String key, String ... values);

    OutboundEvent<T> withHeader(String key, Iterator<String> values);

    OutboundEvent<T> withHeader(Map<String, String> headers);

    OutboundEvent<T> withBody(T body);

    OutboundEvent<T> withDeadLetterSink(Consumer<Message<T>> deadLetterConsumer);

    <V> InboundEvent<Message<V>> send();

    <V> void send(Consumer<Message<V>> responseConsumer);

    Future<Void> emit();

    Future<Void> publish();

    Future<Void> enqueue();
}
