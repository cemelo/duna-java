package io.duna.core;

import io.duna.core.function.Handler;
import io.netty.channel.EventLoop;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;

import java.util.Deque;
import java.util.concurrent.ExecutorService;

public class HandlerManager {

    private MutableMap<Handler<?>, Deque<ExecutorService>> handlerExecutors;

    public HandlerManager() {
        handlerExecutors = new ConcurrentHashMap<>();
    }

    public void register(Handler<?> handler) {
    }

    public EventLoop getExecutor(Handler<?> handler) {
        if (!handlerExecutors.containsKey(handler))
            throw new IllegalStateException("Handler not registered.");

        return null;
    }
}
