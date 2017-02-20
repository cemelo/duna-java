package io.duna.core.eventbus.event;

public enum EventType {
    UNBOUND,

    INBOUND_PUSH,
    INBOUND_QUEUE,
    INBOUND_OBSERVABLE,

    OUTBOUND_PUSH,
    OUTBOUND_QUEUE
}
