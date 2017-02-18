package io.duna.core2.eventbus.event;

import io.duna.core2.eventbus.Message;
import io.duna.core2.function.Consumer;

import io.reactivex.Observable;

import java.util.function.Predicate;

public interface InboundEvent<T> extends Event<Message<T>> {

    @Override
    InboundEvent<T> withCost(int cost);

    @Override
    InboundEvent<T> withFilter(Predicate<? super Message<T>> predicate);

    @Override
    InboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor);

    InboundEvent<T> withErrorSink(Consumer<Message<Throwable>> error);

    void listen(Consumer<Message<T>> eventConsumer);

    Message<T> poll();

    Observable<T> toObservable();
}
