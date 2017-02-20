package io.duna.core.eventbus;

import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;

import java.util.function.Consumer;

public class DefaultEventBus implements EventBus {


    @Override
    public <T> OutboundEvent<T> outbound(String name) {
        return null;
    }

    @Override
    public <T> OutboundEvent<T> outbound(String name, int cost) {
        return null;
    }

    @Override
    public <T> InboundEvent<T> inbound(String name) {
        return null;
    }

    @Override
    public <T> InboundEvent<T> inbound(String name, int cost) {
        return null;
    }

    public void register(Event<?> event) {

    }

    public <T> T poll(String eventName,
                      Consumer<T> resultConsumer,
                      Consumer<Throwable> errorConsumer) {
        return null;
    }
}
