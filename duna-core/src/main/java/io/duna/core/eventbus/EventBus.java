package io.duna.core.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.eventbus.queue.EventQueue;

import java.util.function.Consumer;

public interface EventBus {

    <T> OutboundEvent<T> outbound(String name);

    <T> InboundEvent<T> inbound();

    <T> InboundEvent<T> inbound(String name);

    <T> void queue(String name, EventQueue<T> producer);

    <T> void poll(String queueName, InboundEvent<T> target);

    <T> void process(Message<T> incoming);

    <T> Future<Void> dispatch(Message<T> outgoing, boolean multicast, Consumer<Message<T>> deliveryErrorConsumer);

}
