package io.duna.core2.function;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T offer);
}
