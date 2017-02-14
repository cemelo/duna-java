package io.duna.core.eventbus.impl;

import io.duna.core.eventbus.Event;
import io.duna.core.eventbus.EventBus;
import io.duna.core.eventbus.NodeRegistry;
import io.duna.core.function.Handler;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EventBusImpl implements EventBus {

    private EventRouter eventRouter;
    private NodeRegistry nodeRegistry;

    public EventBusImpl() {
        this.eventRouter = new EventRouter();
    }

    @Override
    public <T> Event<T> event(String name) {
        return new EventImpl<>(this, name);
    }

    @Override
    public CompletableFuture<EventBus> purgeAll() {
        return null;
    }

    @Override
    public CompletableFuture<EventBus> purgeAll(Handler<Set<?>> handler) {
        return null;
    }

    EventRouter getEventRouter() {
        return this.eventRouter;
    }
}
