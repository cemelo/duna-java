package io.duna.core.eventbus.queue;

@FunctionalInterface
public interface EventQueue<T> {
    T poll();

    default boolean isBlocking() {
        return false;
    }

    default boolean isClosed() {
        return false;
    }

    default int getCost() {
        return 0;
    }
}
