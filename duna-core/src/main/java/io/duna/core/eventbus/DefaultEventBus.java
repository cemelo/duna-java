package io.duna.core.eventbus;

import io.duna.core.Duna;
import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.event.DefaultEvent;
import io.duna.core.function.Handler;

import java.util.Set;

public class DefaultEventBus implements EventBus {

    private Duna owner;
    private EndpointDirectory endpointDirectory;

    public DefaultEventBus(Duna owner) {
        this.owner = owner;
    }

    @Override
    public <T> Event<T> event(String name) {
        return new DefaultEvent<>(this, name);
    }

    @Override
    public Future<EventBus> purgeAll() {
        return null;
    }

    @Override
    public Future<EventBus> purgeAll(Handler<Set<?>> handler) {
        return null;
    }

    public Duna getOwner() {
        return this.owner;
    }
}
