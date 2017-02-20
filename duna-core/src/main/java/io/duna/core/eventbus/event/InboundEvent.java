package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;

import io.reactivex.Observable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface InboundEvent<T> extends Event<Message<T>> {

    @Override
    InboundEvent<T> withCost(int cost);

    @Override
    InboundEvent<T> withFilter(Predicate<Message<T>> predicate);

    @Override
    InboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor);

    InboundEvent<T> withErrorSink(Consumer<Message<T>> errorSink);

    void listen(Consumer<Message<T>> eventConsumer);

    Future<T> poll();

    Observable<T> toObservable();

    void accept(Message<T> message);
}
