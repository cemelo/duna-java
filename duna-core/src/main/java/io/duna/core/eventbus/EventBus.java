package io.duna.core.eventbus;

import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.message.Message;
import io.duna.core.function.Handler;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface EventBus {

    <T> Event<T> event(String name);

    EventBus deadMessageHandler(Handler<Message<?>> deadMessageHandler);

    CompletableFuture<EventBus> purge(String eventName);

    CompletableFuture<EventBus> purgeAll();

    CompletableFuture<EventBus> purge(String eventName, Handler<?> handler);

    CompletableFuture<EventBus> purgeAll(Handler<Set<?>> handler);
}
