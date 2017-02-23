package io.duna.core.eventbus.queue;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface MessageQueue2<T> {

    T poll();

    void offer(T offer);

    void close();

    boolean isBlocking();

    boolean isClosed();

    MessageQueue<T> setSupplier(Supplier<? super T> supplier);

    MessageQueue<T> setConsumer(BiConsumer<MessageQueue<T>, ? extends T> consumer);

    MessageQueue<T> setBlocking(boolean blocking);

}
