package io.duna.core.internal.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.EventBus;
import io.duna.core.eventbus.EventRouter;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.internal.eventbus.event.DefaultInboundEvent;
import io.duna.core.internal.eventbus.event.DefaultOutboundEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class LocalEventBus implements EventBus {

    private EventRouter router;

    public LocalEventBus() {
        this.router = new LocalEventRouter();
    }

    @Override
    public <T> OutboundEvent<T> outbound(String name) {
        return new DefaultOutboundEvent<T>(this, name);
    }

    @Override
    public <T> OutboundEvent<T> outbound(String name, int cost) {
        return new DefaultOutboundEvent<T>(this, name).setCost(cost);
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
        return new DefaultInboundEvent<T>(this, name).setCost(cost);
    }

    @Override
    public <T> Future<Void> dispatch(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer) {
        process(outgoing); // Dispatches to the local eventbus
        return Future.completedFuture();
    }

    @Override
    public <T> Future<Void> publish(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer) {

        /* The implementation here is the same as `dispatch` because you can only have
         * one event registered per eventbus node. In distributed environments,
         * this should broadcast the message to all nodes in the cluster.
         */
        process(outgoing);
        return Future.completedFuture();
    }

    @Override
    public <T> void process(Message<T> incoming) {
        // Process the event in the thread corresponding to the event
        Event<?> inbound = router.get(incoming.getTarget());

        if (inbound == null) {
            // Log and exit
            return;
        }

        if (inbound instanceof DefaultInboundEvent) {
            // Should accept in another event loop
            ((DefaultInboundEvent<T>) inbound).accept(incoming);
        }
    }

    public boolean register(Event<?> event) {
        if (router.contains(event.getName()))
            return false;

        router.register(event);
        return true;
    }

    public void cancel(Event<?> event) {
        router.cancel(event);
    }

    public <T> T poll(String queue, Event<?> event) {
        return null;
    }
}
