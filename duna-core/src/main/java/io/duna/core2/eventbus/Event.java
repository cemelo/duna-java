package io.duna.core2.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core2.function.Consumer;
import io.reactivex.Observable;

import java.util.function.Predicate;

public interface Event<T> {

    String getName();

    Event<T> header(String key, String value);

    Event<T> header(String key, String... values);

    Event<T> cost(int cost);

    Event<T> filter(Predicate<? super T> predicate);

    Event<T> deadLetterHandler(Consumer<T> deadLetterConsumer);

    <V> Future<Message<V>> emit(T message);

    <V> void emit(T message, Consumer<Message<V>> replyConsumer);

    Future<Void> publish(T message);

    Future<Void> enqueue(T message);

    Observable<T> observe();

    void listen(Consumer<Message<T>> eventConsumer);

    void poll(Consumer<Message<T>> eventConsumer);
}
