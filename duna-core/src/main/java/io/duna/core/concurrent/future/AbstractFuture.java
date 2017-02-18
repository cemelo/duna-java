package io.duna.core.concurrent.future;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFuture<T> implements Future<T> {

    private final AtomicReference<FutureState> state;

    public AbstractFuture() {
        this.state = new AtomicReference<>(FutureState.NEW);
    }

    @Override
    public boolean done() {
        return state.get() != FutureState.NEW;
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
            throw new IllegalStateException("Future already closed.");
    }

    @Override
    public void complete(T result) {
        if (!state.compareAndSet(FutureState.NEW, FutureState.COMPLETED))
            throw new IllegalStateException("Future already closed.");
    }

    @Override
    public void fail(Throwable error) {
        if (!state.compareAndSet(FutureState.NEW, FutureState.FAILED))
            throw new IllegalStateException("Future already closed.");
    }
}
