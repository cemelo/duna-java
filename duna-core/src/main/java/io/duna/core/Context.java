package io.duna.core;

import io.duna.core.function.Handler;

import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

/**
 * NOT THREAD SAFE!!
 */
public class Context {

    private WeakHashMap<Handler<?>, ExecutorService> handlers;

    public Context() {
        handlers = new WeakHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void execute(Handler<?> handler, Object obj) {
        handlers.computeIfAbsent(handler, h -> {
            return null; // Should get executor from the handler manager
        }).execute(((Handler) handler).toRunnable(obj));
    }
}
