package io.duna.core.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.event.DefaultEvent;
import io.duna.core.function.Handler;

import java.util.Set;

public class DefaultEventBus implements EventBus {

    private EndpointDirectory endpointDirectory;

    public DefaultEventBus() {
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

    void send(Event<?> event) {

    }
}
