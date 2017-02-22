package io.duna.core.eventbus.queue;

public interface EventQueue<T> {

    String getName();

    boolean offer(T item);

    T poll();

}
