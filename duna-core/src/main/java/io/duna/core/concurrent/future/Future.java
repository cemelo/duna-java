package io.duna.core.concurrent.future;

import io.duna.core.internal.concurrent.future.SimpleFuture;

import java.util.function.Consumer;

public interface Future<T> {

    static <T> Future<T> completedFuture() {
        Future<T> future = new SimpleFuture<>();
        future.complete(null);

        return future;
    }

    boolean done();

    boolean completed();

    boolean failed();

    boolean cancelled();

    Future<T> onComplete(Consumer<T> consumer);

    Future<T> onError(Consumer<Throwable> errorConsumer);

    Future<T> onCancel(Consumer<Void> cancellationConsumer);

    void cancel();

    void complete(T result);

    default void complete() {
        complete(null);
    }

    void fail(Throwable error);

}
