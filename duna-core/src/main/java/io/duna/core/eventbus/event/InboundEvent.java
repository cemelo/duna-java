package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.reactivex.Flowable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface InboundEvent<T> extends Event<Message<T>> {

    boolean isBlocking();

    @Override
    InboundEvent<T> setCost(int cost);

    @Override
    InboundEvent<T> setFilter(Predicate<Message<T>> predicate);

    @Override
    InboundEvent<T> setInterceptor(Consumer<Message<T>> interceptor);

    InboundEvent<T> setErrorSink(Consumer<Message<T>> errorSink);

    InboundEvent<T> setBlocking(boolean blocking);

    InboundEvent<T> addListener(Consumer<Message<T>> consumer);

    Future<T> poll(String queue);

    Flowable<T> asFlowable();

}
