package io.duna.core;

import io.netty.channel.EventLoopGroup;

import java.util.concurrent.ExecutorService;

public class DunaOptions {

    private EventLoopGroup eventLoopGroup;
    private ExecutorService workerExecutor;

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public DunaOptions eventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
        return this;
    }

    public ExecutorService getWorkerExecutor() {
        return workerExecutor;
    }

    public DunaOptions workerExecutor(ExecutorService workerExecutor) {
        this.workerExecutor = workerExecutor;
        return this;
    }
}
