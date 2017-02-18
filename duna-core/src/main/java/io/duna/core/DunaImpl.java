package io.duna.core;

import io.duna.core.concurrent.DunaAffinityThreadFactory;
import io.duna.core.concurrent.consumer.ConsumerRegistry;
import io.duna.core.concurrent.future.Future;
import io.duna.core.concurrent.future.SimpleFuture;
import io.duna.core.eventbus.EventBus;
import io.duna.core.service.spi.ServiceProvider;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import net.openhft.affinity.AffinityStrategies;

import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class DunaImpl implements Duna {

    private final DunaOptions options;
    private final EventBus eventBus;
    private final EventLoopGroup eventExecutors;
    private final ExecutorService workerExecutors;

    private final ConsumerRegistry consumerRegistry;

    public DunaImpl(DunaOptions options) {
        this.options = options;

        this.eventBus = null;

        DunaAffinityThreadFactory threadFactory = new DunaAffinityThreadFactory(this,
            AffinityStrategies.ANY);
        this.eventExecutors = new DefaultEventLoopGroup(options.getEventLoops(), threadFactory);
        this.workerExecutors = new ForkJoinPool(options.getWorkers());

        this.consumerRegistry = new ConsumerRegistry(this, eventExecutors, workerExecutors);
    }

    @Override
    public Future<Void> start() {
        final Future<Void> startFuture = new SimpleFuture<>();
        // TODO Implement cancellation

        workerExecutors.execute(() -> {
            ServiceLoader<ServiceProvider> serviceProviders = ServiceLoader.load(ServiceProvider
                .class);
            serviceProviders.forEach(this::deploy);

            startFuture.complete(null);
        });

        return startFuture;
    }

    @Override
    public Future<Void> shutdown() {
        // TODO Implement shutdown flow
        return null;
    }

    EventBus eventBus() {
        return this.eventBus;
    }

    /*
     * Every deployment is executed in the worker pool.
     */
    private Future<Void> deploy(final ServiceProvider serviceProvider) {
        final Future<Void> serviceDeploymentFuture = new SimpleFuture<>();

        workerExecutors.execute(() -> {
            // Set the context before starting
            Context context = new ContextImpl(this);
            ContextImpl.setContext(context);

            serviceProvider.start(serviceDeploymentFuture);
        });

        return serviceDeploymentFuture;
    }
}
