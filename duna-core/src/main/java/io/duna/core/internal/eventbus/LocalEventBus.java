package io.duna.core.internal.eventbus;

import io.duna.core.eventbus.EventBus;
import io.duna.core.eventbus.EventRouter;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.internal.eventbus.event.DefaultInboundEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class LocalEventBus implements EventBus {

    private EventRouter router;

    @Override
    public <T> OutboundEvent<T> outbound(String name) {
        return null;
    }

    @Override
    public <T> OutboundEvent<T> outbound(String name, int cost) {
        return null;
    }

    @Override
    public <T> InboundEvent<T> inbound() {
        String name = UUID.randomUUID().toString();
        return new DefaultInboundEvent<>(this, name);
    }

    @Override
    public <T> InboundEvent<T> inbound(String name) {
        return new DefaultInboundEvent<>(this, name);
    }

    @Override
    public <T> InboundEvent<T> inbound(String name, int cost) {
        return new DefaultInboundEvent<T>(this, name).withCost(cost);
    }

    @Override
    public <T> void dispatch(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer) {
        process(outgoing); // Dispatches to the local eventbus
    }

    @Override
    public <T> void process(Message<T> incoming) {
        Event<?> inbound = router.get(incoming.getTarget());

        if (inbound instanceof DefaultInboundEvent) {

        }
    }

    public void register(Event<?> event) {
        // Registers the event to the router
        router.register(event);
    }

    public void cancel(Event<?> event) {
        router.cancel(event);
    }

    public <T> T poll(String queue, Event<?> event) {
        return null;
    }
}
