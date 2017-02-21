package io.duna.core.eventbus;

import io.duna.core.eventbus.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EventRouter {

    void register(@NotNull Event<?> event);

    void cancel(@NotNull String eventName);

    default void cancel(Event<?> event) {
        cancel(event.getName());
    }

    @Nullable
    Event<?> get(String eventName);

    boolean contains(String eventName);

}
