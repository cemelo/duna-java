package io.duna.core.eventbus;

import io.duna.core.function.Handler;

import io.netty.channel.EventLoop;
import org.eclipse.collections.api.bimap.BiMap;

public class EventRouter {

    private BiMap<EventLoop, Handler<?>> handlers;
}
