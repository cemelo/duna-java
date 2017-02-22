package io.duna.core.internal.eventbus;

import io.duna.core.eventbus.EventRouter;
import io.duna.core.eventbus.event.InboundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalEventRouter implements EventRouter {

    private Map<String, InboundEvent<?>> events;

    LocalEventRouter() {
        this.events = new ConcurrentHashMap<>();
    }

    @Override
    public void register(InboundEvent<?> event) {
        if (events.containsKey(event.getName()))
            throw new IllegalArgumentException("Event " + event.getName() + " is already " +
                "registered.");

        events.put(event.getName(), event);
    }

    @Override
    public void cancel(@NotNull String eventName) {
        if (!events.containsKey(eventName))
            throw new IllegalArgumentException("Event " + eventName + " isn't registered.");

        events.remove(eventName);
    }

    @Override
    public InboundEvent<?> get(String eventName) {
        return events.get(eventName);
    }

    @Override
    public boolean contains(String eventName) {
        return events.containsKey(eventName);
    }
}
