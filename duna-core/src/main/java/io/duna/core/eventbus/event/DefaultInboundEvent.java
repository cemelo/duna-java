package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.concurrent.future.SimpleFuture;
import io.duna.core.eventbus.DefaultEventBus;
import io.duna.core.eventbus.Message;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultInboundEvent<T> implements InboundEvent<T> {

    private EventType eventType;

    private final DefaultEventBus eventBus;
    private final String name;
    private int cost;

    private Predicate<Message<T>> filter;
    private Consumer<Message<T>> interceptor;

    private Consumer<Message<T>> errorSink;
    private Consumer<Message<T>> eventConsumer;
    private ObservableEmitter<T> messageEmitter;
    private Future<T> queueFuture;

    private final AtomicBoolean registered;

    public DefaultInboundEvent(DefaultEventBus eventBus, String name) {
        this.eventBus = eventBus;
        this.name = name;
        this.registered = new AtomicBoolean(false);
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
    public void listen(Consumer<Message<T>> eventConsumer) {
        if (!registered.compareAndSet(false, true))
            throw new IllegalStateException("Event already registered.");

        // register the event using the event bus
        eventBus.register(this);
    }

    @Override
    public Future<T> poll() {
        if (!registered.compareAndSet(false, true))
            throw new IllegalStateException("Event already registered.");

        eventBus.register(this);

        Future<T> future = new SimpleFuture<>();
        eventBus.<T> poll(name,
            future::complete,
            future::fail);

        return future;
    }

    @Override
    public Observable<T> toObservable() {
        if (!registered.compareAndSet(false, true))
            throw new IllegalStateException("Event already registered.");

        eventBus.register(this);

        if (this.messageEmitter == null) {

        }

        return Observable.create(emitter -> {

        });
    }

    @Override
    public void accept(Message<T> message) {
        emitToObservable(message);

        if (message.failed() && errorSink != null) {
            errorSink.accept(message);
        } else {
            eventConsumer.accept(message);
        }
    }

    private void emitToObservable(Message<T> message) {
        if (messageEmitter == null) return;

        if (message.failed()) {
            messageEmitter.onError(message.getCause());
        } else {
            if (messageEmitter.isDisposed()) return;

            messageEmitter.onNext(message.getBody());

            boolean complete = message.getHeaders()
                .getOrDefault("stream-state", "open")
                .equals("complete");

            if (complete) messageEmitter.onComplete();
        }
    }
}
