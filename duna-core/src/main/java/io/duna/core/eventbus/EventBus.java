package io.duna.core.eventbus;

import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;

import java.util.function.Consumer;

public interface EventBus {

    <T> OutboundEvent<T> outbound(String name);

    <T> OutboundEvent<T> outbound(String name, int cost);

    <T> InboundEvent<T> inbound();

    <T> InboundEvent<T> inbound(String name);

    <T> InboundEvent<T> inbound(String name, int cost);

    <T> void dispatch(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer);

    <T> void process(Message<T> incoming);

}
