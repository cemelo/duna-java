package io.duna.core.eventbus.impl;

import io.duna.core.concurrent.CompositeFuture;
import io.duna.core.concurrent.Future;
import io.duna.core.concurrent.impl.AbstractFuture;
import io.duna.core.function.Handler;
import io.netty.channel.EventLoop;

public class FutureEvent<T> extends AbstractFuture<T> {

    private String name;
    private EventLoop executor;
    private Handler<T> completionHandler;
    private Handler<? extends Throwable> errorHandler;
    private Handler<Void> cancellationHandler;

    public FutureEvent(String name, EventLoop eventLoop) {
        this.name = name;
        this.executor = eventLoop;
    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public Future<T> onComplete(Handler<T> handler) {
        this.completionHandler = handler;

        if (completed()) {
            executor.execute(() -> handler.handle(result));
        }

        return this;
    }

    @Override
    public Future<T> onError(Handler<? extends Throwable> errorHandler) {
        return this;
    }

    @Override
    public Future<T> onCancel(Handler<Void> cancellationHandler) {
        return this;
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void complete(T result) {
        super.complete(result);
    }

    @Override
    public void fail(Throwable error) {
        super.fail(error);
    }

    @Override
    public <V> CompositeFuture andThen(Handler<V> handler) {
        return null;
    }
}
