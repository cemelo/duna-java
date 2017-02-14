package io.duna.core.concurrent.impl;

import io.duna.core.concurrent.Future;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFuture<T> implements Future<T> {

    protected T result;
    protected Throwable error;
    private AtomicReference<FutureState> futureState;

    @Override
    public boolean completed() {
        return futureState.get() == FutureState.COMPLETED;
    }

    @Override
    public boolean failed() {
        return futureState.get() == FutureState.FAILED;
    }

    @Override
    public void cancel() {
        if (!futureState.compareAndSet(FutureState.NEW, FutureState.CANCELLED))
            throw new IllegalStateException("Future already completed.");
    }

    @Override
    public void complete(T result) {
        if (!futureState.compareAndSet(FutureState.NEW, FutureState.COMPLETED))
            throw new IllegalStateException("Future already completed.");
    }

    @Override
    public void fail(Throwable error) {
        if (!futureState.compareAndSet(FutureState.NEW, FutureState.FAILED))
            throw new IllegalStateException("Future already completed.");
    }
}
