package io.duna.core.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.message.Message;
import io.duna.core.function.Handler;
import io.reactivex.Observable;

import java.util.function.Predicate;

public interface Event<T> {

    Event<T> header(String key, String value);

    Event<T> header(String key, String ... values);

    Event<T> filter(Predicate<? super T> predicate);

    Event<T> deadLetterHandler(Handler<T> handler);

    String getName();

    Future<Void> purge();

    Future<Void> purge(Handler<?> handler);

    <V> Future<Message<V>> send(T message);

    Future<Void> publish(T message);

    Future<Void> enqueue(T message);

    void consume(Handler<Message<T>> consumer);

    default void dequeue(Handler<Message<T>> handler) {
        dequeue(handler, 0);
    }

    void dequeue(Handler<Message<T>> consumer, long interval);

    Observable<T> observe();
}
