package io.duna.core2.concurrent.future;

import io.duna.core2.function.Consumer;

public interface Future<T> {

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
