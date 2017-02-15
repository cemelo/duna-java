package io.duna.core;

import io.netty.channel.EventLoopGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public class Duna {

    private AtomicReference<Void> state;
    private EventLoopGroup executor;
    private ExecutorService workerExecutor;
    private HandlerManager handlerManager;
    private DunaOptions options;

    private Duna(DunaOptions options) {
        executor = options.getEventLoopGroup();
        workerExecutor = options.getWorkerExecutor();
    }

    public static Duna currentInstance() {
        // NOOP
        return null;
    }
}
