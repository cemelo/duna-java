package io.duna.core;

import io.duna.core.concurrent.RoundRobinSelector;
import io.duna.core.function.Handler;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.EventExecutor;
import kotlin.Pair;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;

import java.util.Collections;
import java.util.Deque;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

public class HandlerManager {

    private MutableMap<Handler<?>, RoundRobinSelector<ExecutorService>> handlerExecutors;

    private RoundRobinSelector<EventExecutor> executors;

    public HandlerManager() {
        handlerExecutors = new ConcurrentHashMap<>();
    }

    public void register(Handler<?> handler) {
        ExecutorService executor = executors.next();

        handlerExecutors
            .computeIfAbsent(handler, (k) -> new RoundRobinSelector<>(Collections.emptySet()))
            .add(executor);
    }

    public ExecutorService getExecutor(Handler<?> handler) {
        if (!handlerExecutors.containsKey(handler))
            throw new IllegalStateException("Handler not registered.");

        return handlerExecutors
            .get(handler)
            .next();
    }
}
