package io.duna.core.concurrent;

import io.duna.core.function.Handler;
import io.netty.channel.EventLoop;
import org.eclipse.collections.api.bimap.BiMap;

public class HandlerExecutor {

    private BiMap<EventLoop, Handler<?>> handlerExecutors;

    public void dispatch(Handler<?> handler) {

    }

    public EventLoop register(Handler<?> handler) {
        return null;
    }

    public EventLoop register(Handler<?> handler, EventLoop eventLoop) {
        return null;
    }
}
