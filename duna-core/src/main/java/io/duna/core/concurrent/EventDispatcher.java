package io.duna.core.concurrent;

import io.duna.core.function.Handler;

import io.netty.channel.EventLoop;

import java.util.Map;

public class EventDispatcher {

    Map<EventLoop, Handler> handlers;


}
