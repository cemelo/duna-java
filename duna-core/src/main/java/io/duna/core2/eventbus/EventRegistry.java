package io.duna.core2.eventbus;

import java.net.InetAddress;

public interface EventRegistry {

    void register(Event<?> event);

    void remove(String eventName);

    default void remove(Event<?> event) {
        remove(event.getName());
    }

    InetAddress getNode(Event<?> event);

    Iterable<InetAddress> getAllNodes(Event<?> event);

}
