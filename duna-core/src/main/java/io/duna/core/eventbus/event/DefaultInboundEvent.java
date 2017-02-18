package io.duna.core.eventbus.event;

import io.duna.core.eventbus.Message;
import io.duna.core.function.Consumer;

import io.reactivex.Observable;

import java.util.List;
import java.util.function.Predicate;

public class DefaultInboundEvent<T> implements InboundEvent<T> {

    private String name;
    private int cost;
    private List<Predicate<? super Message<T>>> filters;
    private List<Consumer<Message<T>>> interceptors;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InboundEvent<T> withCost(int cost) {
        this.cost = cost;
        return this;
    }

    @Override
    public InboundEvent<T> withFilter(Predicate<? super Message<T>> predicate) {
        filters.add(predicate);
        return this;
    }

    @Override
    public InboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    @Override
    public InboundEvent<T> withErrorSink(Consumer<Message<Throwable>> error) {
        return this;
    }

    @Override
    public void listen(Consumer<Message<T>> eventConsumer) {

    }

    @Override
    public Message<T> poll() {
        return null;
    }

    @Override
    public Observable<T> toObservable() {
        return null;
    }
}
