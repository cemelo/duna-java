package io.duna.core.internal.concurrent.future;

import io.duna.core.concurrent.Future;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SimpleFuture<T> extends AbstractFuture<T> {

    private volatile T result;
    private volatile Throwable error;

    private Consumer<T> completionConsumer;
    private Consumer<Throwable> errorConsumer;
    private Consumer<Void> cancellationConsumer;

    private final AtomicBoolean consumerInvoked;

    public SimpleFuture() {
        this.consumerInvoked = new AtomicBoolean(false);
    }

    @Override
    public void cancel() {
        super.cancel();
        java.util.concurrent.Future
        if (cancellationConsumer != null && consumerInvoked.compareAndSet(false, true)) {
            cancellationConsumer.accept(null);
        }
    }

    @Override
    public void complete(T result) {
        super.complete(result);
        this.result = result;

        if (completionConsumer != null && consumerInvoked.compareAndSet(false, true)) {
            completionConsumer.accept(result);
        }
    }

    @Override
    public void fail(Throwable error) {
        super.fail(error);
        this.error = error;

        if (errorConsumer != null && consumerInvoked.compareAndSet(false, true)) {
            errorConsumer.accept(error);
        }
    }

    @NotNull
    @Override
    public Future<T> onComplete(@NotNull Consumer<? super T> consumer) {
        this.completionConsumer = consumer;

        if (completed() && consumerInvoked.compareAndSet(false, true)) {
            consumer.accept(result);
        }

        return this;
    }

    @NotNull
    @Override
    public Future<T> onError(@NotNull BiConsumer<? super T, ? super Throwable> errorConsumer) {
        this.errorConsumer = errorConsumer;

        if (failed() && consumerInvoked.compareAndSet(false, true)) {
            errorConsumer.accept(error);
        }

        return this;
    }

    @NotNull
    @Override
    public Future<T> onCancel(@NotNull Consumer<Unit> cancellationConsumer) {
        this.cancellationConsumer = cancellationConsumer;

        if (cancelled() && consumerInvoked.compareAndSet(false, true)) {
            cancellationConsumer.accept(null);
        }

        return this;
    }
}
