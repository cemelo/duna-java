package io.duna.core.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.eventbus.queue.EventQueue;

import java.util.function.Consumer;

public interface EventBus {

    <T> OutboundEvent<T> outbound(String name);

    <T> InboundEvent<T> inbound(String name);

    <T> Future<Void> dispatch(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer);

    <T> Future<Void> publish(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer);

    <T> void registerQueue(EventQueue<T> producer);

    <T> void process(Message<T> incoming);

}
