package io.duna.core;

import io.duna.core.function.Handler;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;

import java.util.concurrent.ExecutorService;

public class HandlerManager {

    private MutableMap<Handler<?>, EventExecutor> handlerExecutorMapping;
    private MutableMap<Handler<?>, Void> blockingHandlers;

    private EventExecutorGroup handlerExecutors;
    private ExecutorService workerExecutor;

    public HandlerManager(EventExecutorGroup handlerExecutors, ExecutorService workerExecutor) {
        this.workerExecutor = workerExecutor;
        this.handlerExecutors = handlerExecutors;

        handlerExecutorMapping = new ConcurrentHashMap<>();
        blockingHandlers = new ConcurrentHashMap<>();
    }

    public void register(Handler<?> handler) {
        register(handler, false);
    }

    public void register(Handler<?> handler, boolean blocking) {
        if (handlerExecutorMapping.containsKey(handler) || blockingHandlers.containsKey(handler))
            throw new IllegalArgumentException("Handler is already registered.");

        if (blocking) {
            blockingHandlers.put(handler, null);
        } else {
            handlerExecutorMapping.putIfAbsent(handler, handlerExecutors.next());
        }
    }

    public void unregister(Handler<?> handler) {
        handlerExecutorMapping.removeKey(handler);
        blockingHandlers.removeKey(handler);
    }

    public EventExecutor getExecutor(Handler<?> handler) {
        if (!handlerExecutorMapping.containsKey(handler))
            throw new IllegalStateException("Handler not registered.");

        return handlerExecutorMapping.get(handler);
    }

    public <T> void execute(Task<T> task) {
        if (!handlerExecutorMapping.containsKey(task.getHandler()) && !blockingHandlers.containsKey(task.getHandler()))
            throw new IllegalArgumentException("Handler not registered.");

        if (blockingHandlers.containsKey(task.getHandler())) {
            workerExecutor.execute(() -> task.getHandler().handle(task.getArgument()));
        } else {
            EventExecutor executor = getExecutor(task.getHandler());

            if (executor.inEventLoop()) {
                task.getHandler().handle(task.getArgument());
            } else {
                executor.execute(() -> task.getHandler().handle(task.getArgument()));
            }
        }
    }
}
