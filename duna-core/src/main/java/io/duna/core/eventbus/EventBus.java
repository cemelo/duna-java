package io.duna.core.eventbus;

import io.duna.core.concurrent.future.Future;
import io.duna.core.function.Handler;

import java.util.Set;

public interface EventBus {

    <T> Event<T> event(String name);

    Future<EventBus> purgeAll();

    Future<EventBus> purgeAll(Handler<Set<?>> handler);
}
