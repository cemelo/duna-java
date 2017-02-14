package io.duna.core.concurrent;

import io.duna.core.function.Handler;

public interface Future<T> {

    boolean completed();

    boolean failed();

    Future<T> onComplete(Handler<T> handler);

    Future<T> onError(Handler<? extends Throwable> errorHandler);

    Future<T> onCancel(Handler<Void> cancellationHandler);

    T get();

    void cancel();

    void complete(T result);

    void fail(Throwable error);

    <V> CompositeFuture andThen(Handler<V> handler);
}
