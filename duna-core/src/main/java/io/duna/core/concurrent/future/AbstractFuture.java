package io.duna.core.concurrent.future;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFuture<T> implements Future<T> {

    private final AtomicReference<FutureState> state;

    protected AbstractFuture() {
        state = new AtomicReference<>(FutureState.NEW);
    }

    @Override
    public boolean completed() {
        return state.get() == FutureState.COMPLETED;
    }

    @Override
    public boolean failed() {
        return state.get() == FutureState.FAILED;
    }

    @Override
    public boolean cancelled() {
        return state.get() == FutureState.CANCELLED;
    }

    @Override
    public void cancel() {
        if (!state.compareAndSet(FutureState.NEW, FutureState.CANCELLED))
            throw new IllegalStateException("Future already completed.");
    }

    @Override
    public void complete(T result) {
        if (!state.compareAndSet(FutureState.NEW, FutureState.COMPLETED))
            throw new IllegalStateException("Future already completed.");
    }

    @Override
    public <V extends Throwable> void fail(V error) {
        if (!state.compareAndSet(FutureState.NEW, FutureState.FAILED))
            throw new IllegalStateException("Future already completed.");
    }
}