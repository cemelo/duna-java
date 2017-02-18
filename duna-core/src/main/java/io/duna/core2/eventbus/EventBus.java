package io.duna.core2.eventbus;

import io.duna.core2.eventbus.event.Event;
import io.duna.core2.eventbus.event.InboundEvent;
import io.duna.core2.eventbus.event.OutboundEvent;

public interface EventBus {

    <T> OutboundEvent<T> outbound(String name);

    <T> OutboundEvent<T> outbound(String name, int cost);

    <T> InboundEvent<T> inbound(String name);

    <T> InboundEvent<T> inbound(String name, int cost);

    void dispatch(Event<?> event);

}
