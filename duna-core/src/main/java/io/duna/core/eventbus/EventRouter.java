package io.duna.core.eventbus;

import io.duna.core.eventbus.event.InboundEvent;
import org.jetbrains.annotations.NotNull;

public interface EventRouter {

    void register(InboundEvent<?> event);

    void cancel(@NotNull String eventName);

    default void cancel(InboundEvent<?> event) {
        cancel(event.getName());
    }

    InboundEvent<?> get(String eventName);

    boolean contains(String eventName);

}
