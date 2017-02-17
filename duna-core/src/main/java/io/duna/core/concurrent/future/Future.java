package io.duna.core.concurrent.future;

import io.duna.core.function.Handler;

public interface Future<T> {

    boolean completed();

    boolean failed();

    boolean cancelled();

    Future<T> onComplete(Handler<T> handler);

    <V extends Throwable> Future<T> onError(Handler<V> errorHandler);

    Future<T> onCancel(Handler<Void> cancellationHandler);

    void cancel();

    void complete(T result);

    default void complete() {
        complete(null);
    }

    <V extends Throwable> void fail(V error);

    <V> CompositeFuture andThen(Handler<V> handler);
}
