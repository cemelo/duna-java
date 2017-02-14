package io.duna.core.eventbus.event;

import io.duna.core.eventbus.message.Message;
import io.duna.core.function.Handler;

import io.reactivex.Observable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public interface Event<T> {

    Event<T> header(String key, String value);

    Event<T> header(String key, String ... values);

    Event<T> filter(Predicate<? super T> predicate);

    <V> CompletableFuture<Message<V>> send(T message);

    Future<Void> publish(T message);

    Future<Void> enqueue(T message);

    void consume(Handler<Message<T>> consumer);

    void probe(Handler<Message<T>> consumer, TimeUnit interval);

    Observable<T> observe();
}
