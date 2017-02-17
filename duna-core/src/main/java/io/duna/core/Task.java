package io.duna.core;

import io.duna.core.function.Handler;

public class Task<T> {

    private Handler<T> handler;
    private T argument;

    public Task(Handler<T> handler, T argument) {
        this.handler = handler;
        this.argument = argument;
    }

    public Handler<T> getHandler() {
        return handler;
    }

    public T getArgument() {
        return argument;
    }
}
