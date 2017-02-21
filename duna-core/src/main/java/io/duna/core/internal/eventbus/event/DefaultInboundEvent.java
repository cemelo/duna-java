package io.duna.core.internal.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.internal.concurrent.future.SimpleFuture;
import io.duna.core.internal.eventbus.LocalEventBus;
import io.duna.core.util.internal;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public
@internal
class DefaultInboundEvent<T> implements InboundEvent<T> {

    private final LocalEventBus eventBus;
    private final String name;
    private int cost;

    private Predicate<Message<T>> filter;
    private Consumer<Message<T>> interceptor;

    private Consumer<Message<T>> errorSink;
    private final AtomicReference<Consumer<Message<T>>> eventSink;

    private Flowable<T> flowable;

    public DefaultInboundEvent(LocalEventBus eventBus, String name) {
        this.eventBus = eventBus;
        this.eventSink = new AtomicReference<>(m -> {});
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public Predicate<Message<T>> getFilter() {
        return filter;
    }

    @Override
    public Consumer<Message<T>> getInterceptor() {
        return interceptor;
    }

    @Override
    public InboundEvent<T> withCost(int cost) {
        this.cost = cost;
        return this;
    }

    @Override
    public InboundEvent<T> withFilter(Predicate<Message<T>> predicate) {
        this.filter = predicate;
        return this;
    }

    @Override
    public InboundEvent<T> withInterceptor(Consumer<Message<T>> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public InboundEvent<T> withErrorSink(Consumer<Message<T>> errorSink) {
        this.errorSink = errorSink;
        return this;
    }

    @Override
    public void addListener(Consumer<Message<T>> consumer) {
        Objects.requireNonNull(consumer, () -> "The consumer cannot be null.");
        this.eventSink.updateAndGet(c -> c.andThen(consumer));

        eventBus.register(this);
    }

    @Override
    public Future<T> poll(String queue) {
        Objects.requireNonNull(queue, () -> "The target queue name cannot be null.");

        Future<T> future = new SimpleFuture<>();
        this.asFlowable()
            .take(1)
            .doOnError(future::fail)
            .subscribe(future::complete);

        eventBus.poll(queue, this);

        return future;
    }

    @Override
    public Flowable<T> asFlowable() {
        if (this.flowable != null)
            return this.flowable;

        return this.flowable = Flowable.<T>create(
            emitter -> {
                eventBus.register(this);

                eventSink.updateAndGet(c -> c.andThen(m -> {
                        if (emitter.isCancelled()) return;

                        if (m.failed()) emitter.onError(m.getCause());
                        if (m.succeeded()) emitter.onNext(m.getBody());
                    }));

                emitter.setCancellable(() -> eventBus.cancel(this));
            }, BackpressureStrategy.BUFFER)
            .publish()
            .refCount();
    }

    public void accept(Message<T> message) {
        Objects.requireNonNull(message, () -> "Message cannot be null.");

        if (message.failed() && errorSink != null) {
            errorSink.accept(message);
        }

        eventSink.get().accept(message);
    }
}
