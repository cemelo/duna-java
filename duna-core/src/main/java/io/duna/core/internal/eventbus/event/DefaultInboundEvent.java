package io.duna.core.internal.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.internal.concurrent.future.SimpleFuture;
import io.duna.core.internal.eventbus.MultithreadLocalEventBus;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultInboundEvent<T> implements InboundEvent<T> {

    private final MultithreadLocalEventBus eventBus;
    private final String name;
    private boolean blocking;

    private Predicate<Message<T>> filter;
    private Consumer<Message<T>> interceptor;

    private Consumer<Message<T>> errorSink;
    private final AtomicReference<Consumer<Message<T>>> eventSink;

    private Flowable<T> flowable;

    public DefaultInboundEvent(MultithreadLocalEventBus eventBus, String name) {
        this.eventBus = eventBus;
        this.eventSink = new AtomicReference<>(m -> {});
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
    public boolean isBlocking() {
        return blocking;
    }

    @Override
    public InboundEvent<T> setFilter(Predicate<Message<T>> predicate) {
        this.filter = predicate;
        return this;
    }

    @Override
    public InboundEvent<T> setInterceptor(Consumer<Message<T>> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public InboundEvent<T> setErrorSink(Consumer<Message<T>> errorSink) {
        this.errorSink = errorSink;
        return this;
    }

    @Override
    public InboundEvent<T> setBlocking(boolean blocking) {
        this.blocking = blocking;
        return this;
    }

    @Override
    public InboundEvent<T> addListener(Consumer<Message<T>> consumer) {
        Objects.requireNonNull(consumer, () -> "The consumer cannot be null.");
        this.eventSink.updateAndGet(c -> c.andThen(consumer));

        eventBus.register(this);
        return this;
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

    @Override
    public void accept(Message<T> message) {
        Objects.requireNonNull(message, () -> "Message cannot be null.");

        if (interceptor != null)
            interceptor.accept(message);

        if (filter != null && !filter.test(message)) return;

        if (message.failed() && errorSink != null) {
            System.out.println("Aceitando erro");
            errorSink.accept(message);
        }

        eventSink.get().accept(message);
    }
}
