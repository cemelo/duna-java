package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.AbstractFuture;
import io.duna.core.concurrent.future.CompositeFuture;
import io.duna.core.concurrent.future.Future;
import io.duna.core.function.Handler;
import io.netty.channel.EventLoop;

import java.util.concurrent.atomic.AtomicBoolean;

public class FutureEvent<T> extends AbstractFuture<T> {

    private String name;
    private EventLoop executor;

    private volatile T result;
    private volatile Throwable error;
    private volatile Handler<T> completionHandler;
    private volatile Handler<? extends Throwable> errorHandler;
    private volatile Handler<Void> cancellationHandler;

    private final AtomicBoolean listenerCalled;

    public FutureEvent(String name, EventLoop eventLoop) {
        this.name = name;
        this.executor = eventLoop;
        this.listenerCalled = new AtomicBoolean(false);
    }

    @Override
    public Future<T> onComplete(Handler<T> handler) {
        this.completionHandler = handler;

        if (completed()) {
            if (listenerCalled.compareAndSet(false, true)) {
                executor.execute(() -> handler.handle(result));
            }
        }

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends Throwable> Future<T> onError(Handler<V> errorHandler) {
        this.errorHandler = errorHandler;

        if (failed()) {
            if (listenerCalled.compareAndSet(false, true)) {
                executor.execute(() -> errorHandler.handle((V) error));
            }
        }

        return this;
    }

    @Override
    public Future<T> onCancel(Handler<Void> cancellationHandler) {
        this.cancellationHandler = cancellationHandler;

        if (cancelled()) {
            if (listenerCalled.compareAndSet(false, true)) {
                executor.execute(() -> cancellationHandler.handle(null));
            }
        }

        return this;
    }

    @Override
    public void cancel() {
        super.cancel();

        if (this.cancellationHandler != null && listenerCalled.compareAndSet(false, true)) {
            executor.execute(() -> cancellationHandler.handle(null));
        }
    }

    @Override
    public void complete(T result) {
        super.complete(result);

        this.result = result;
        if (this.completionHandler != null && listenerCalled.compareAndSet(false, true)) {
            executor.execute(() -> completionHandler.handle(result));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends Throwable> void fail(V error) {
        super.fail(error);

        this.error = error;
        if (this.errorHandler != null && listenerCalled.compareAndSet(false, true)) {
            executor.execute(() -> ((Handler<Throwable>) errorHandler).handle(error));
        }
    }

    @Override
    public <V> CompositeFuture andThen(Handler<V> handler) {
        throw new UnsupportedOperationException();
    }
}
