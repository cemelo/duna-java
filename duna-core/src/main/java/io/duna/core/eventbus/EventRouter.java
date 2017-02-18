package io.duna.core.eventbus;

import io.duna.core.eventbus.event.Event;

import java.net.InetAddress;

public interface EventRouter {

    void register(Event<?> event);

    void remove(String eventName);

    default void remove(Event<?> event) {
        remove(event.getName());
    }

    InetAddress getNode(Event<?> event);

    Iterable<InetAddress> getAllNodes(Event<?> event);

}
