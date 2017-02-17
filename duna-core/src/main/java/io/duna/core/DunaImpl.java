package io.duna.core;

import io.duna.core.concurrent.DunaEventLoopGroup;
import io.duna.core.eventbus.DefaultEventBus;
import io.duna.core.eventbus.EventBus;
import io.duna.core.spi.service.ServiceProvider;

import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class DunaImpl implements Duna {

    private DunaOptions options;

    private DunaEventLoopGroup eventExecutors;
    private ExecutorService workerPool;

    private EventBus eventBus;

    DunaImpl(DunaOptions options) {
        this.options = options;
    }

    private void deploy(ServiceProvider service) {
        eventExecutors.execute(() -> {
            Context context = new DefaultContext(this);
        });
    }

    @Override
    public void start() {
        eventExecutors = new DunaEventLoopGroup(options.getEventLoopThreads());
        eventBus = new DefaultEventBus(this);

        if (options.isUseCommonWorkerPool())
            workerPool = ForkJoinPool.commonPool();
        else
            workerPool = new ForkJoinPool();

        ServiceLoader<ServiceProvider> providers = ServiceLoader.load(ServiceProvider.class);
        providers.forEach(this::deploy);
    }

    @Override
    public EventBus eventBus() {
        return eventBus;
    }

    @Override
    public HandlerManager handlerManager() {
        return null;
    }
}
