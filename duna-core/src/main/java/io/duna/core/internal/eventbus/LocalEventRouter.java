package io.duna.core.internal.eventbus;

import io.duna.core.eventbus.EventRouter;
import io.duna.core.eventbus.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class LocalEventRouter implements EventRouter {

    private Map<String, Event<?>> events;

    private Map<Event<?>, ExecutorService> eventExecutors;
    private ExecutorService workerExecutor;

    LocalEventRouter() {
        this.events = new ConcurrentHashMap<>();
    }

    @Override
    public void register(@NotNull Event<?> event) {
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

    @Nullable
    @Override
    public Event<?> get(String eventName) {
        return events.get(eventName);
    }

    @Override
    public boolean contains(String eventName) {
        return events.containsKey(eventName);
    }
}
