package io.duna.core.function;

@FunctionalInterface
public interface Handler<T> {
    void handle(T obj);
}