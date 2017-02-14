package io.duna.core.eventbus;

import io.duna.core.function.Handler;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface EventBus {

    <T> Event<T> event(String name);

    CompletableFuture<EventBus> purgeAll();

    CompletableFuture<EventBus> purgeAll(Handler<Set<?>> handler);
}
