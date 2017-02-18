package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.duna.core.function.Consumer;

import io.reactivex.Observable;

import java.util.function.Predicate;

public class FutureInboundEvent<T> implements InboundEvent<T>, Future<T> {

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public boolean completed() {
        return false;
    }

    @Override
    public boolean failed() {
        return false;
    }

    @Override
    public boolean cancelled() {
        return false;
    }

    @Override
    public Future<T> onComplete(Consumer<T> consumer) {
        return null;
    }

    @Override
    public Future<T> onError(Consumer<Throwable> errorConsumer) {
        return null;
    }

    @Override
    public Future<T> onCancel(Consumer<Void> cancellationConsumer) {
        return null;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void complete(T result) {

    }

    @Override
    public void fail(Throwable error) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public InboundEvent<T> withCost(int cost) {
        return null;
    }

    @Override
    public InboundEvent<T> withFilter(Predicate<? super Message<T>> predicate) {
        return null;
    }

    @Override
    public InboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor) {
        return null;
    }

    @Override
    public InboundEvent<T> withErrorSink(Consumer<Message<Throwable>> error) {
        return null;
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
