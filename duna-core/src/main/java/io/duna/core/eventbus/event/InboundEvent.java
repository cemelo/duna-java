package io.duna.core.eventbus.event;

import io.duna.core.concurrent.Future;
import io.duna.core.eventbus.Message;
import io.reactivex.Flowable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface InboundEvent2<T> extends Event2<Message<T>> {

    Future<T> poll(String queue);

    Flowable<T> asFlowable();

    void accept(Message<T> message);

    boolean isBlocking();

    @Override
    InboundEvent2<T> setFilter(Predicate<Message<T>> predicate);

    @Override
    InboundEvent2<T> setInterceptor(Consumer<Message<T>> interceptor);

    InboundEvent2<T> setErrorSink(Consumer<Message<T>> errorSink);

    InboundEvent2<T> setBlocking(boolean blocking);

    InboundEvent2<T> addListener(Consumer<Message<T>> consumer);
}
