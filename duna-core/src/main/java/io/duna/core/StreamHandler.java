package io.duna.core;

import io.reactivex.Observable;

@FunctionalInterface
public interface StreamHandler<T> {

    void handle(Observable<T> observable);
}
