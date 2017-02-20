package io.duna.core.eventbus.event;

import io.duna.core.util.Internal;

import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

@Internal
public class EventEmitter<T> implements ObservableEmitter<T> {

    private Collection<ObservableEmitter<T>> children;

    public EventEmitter() {
        this.children = new ConcurrentSkipListSet<>();
    }

    public void addChildEmitter(ObservableEmitter<T> child) {
        children.add(child);
    }

    @Override
    public void setDisposable(Disposable d) {
        children.forEach(c -> c.setDisposable(d));
    }

    @Override
    public void setCancellable(Cancellable c) {
        children.forEach(ch -> ch.setCancellable(c));
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    @Override
    public ObservableEmitter<T> serialize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onNext(T value) {
        children.forEach(c -> c.onNext(value));
    }

    @Override
    public void onError(Throwable error) {
        children.forEach(c -> c.onError(error));
    }

    @Override
    public void onComplete() {
        children.forEach(ObservableEmitter::onComplete);
    }
}
