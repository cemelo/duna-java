package io.duna.core2.eventbus;

import io.duna.core2.eventbus.event.Event;

public class DefaultEventBus implements EventBus {

    @Override
    public <T> Event<T> event(String name) {
        return null;
    }

    @Override
    public void dispatch(Event<?> event) {

    }
}
