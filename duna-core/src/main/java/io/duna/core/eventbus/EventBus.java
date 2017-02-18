package io.duna.core.eventbus;

import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;

public interface EventBus {

    <T> OutboundEvent<T> outbound(String name);

    <T> OutboundEvent<T> outbound(String name, int cost);

    <T> InboundEvent<T> inbound(String name);

    <T> InboundEvent<T> inbound(String name, int cost);

    void dispatch(Event<?> event);

}
