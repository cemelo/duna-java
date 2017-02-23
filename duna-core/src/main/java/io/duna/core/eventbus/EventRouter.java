package io.duna.core.eventbus;

import io.duna.core.eventbus.event.Subscriber;
import org.jetbrains.annotations.NotNull;

public interface EventRouter {

    void register(Subscriber<?> event);

    void cancel(@NotNull String eventName);

    default void cancel(Subscriber<?> event) {
        cancel(event.getName());
    }

    Subscriber<?> get(String eventName);

    boolean contains(String eventName);

}
