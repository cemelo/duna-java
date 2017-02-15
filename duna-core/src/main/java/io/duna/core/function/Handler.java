package io.duna.core.function;

@FunctionalInterface
public interface Handler<T> {
    void handle(T obj);

    default Runnable toRunnable(T obj) {
        return () -> {
            this.handle(obj);
        };
    }
}
