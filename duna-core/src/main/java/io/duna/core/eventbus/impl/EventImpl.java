package io.duna.core.eventbus.impl;

import io.duna.core.concurrent.Future;
import io.duna.core.eventbus.Event;
import io.duna.core.eventbus.exception.EventResponseException;
import io.duna.core.eventbus.message.Message;
import io.duna.core.function.Handler;
import io.reactivex.Observable;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class EventImpl<T> implements Event<T> {

    private EventBusImpl eventBus;

    private String name;

    private InetAddress node;

    private MutableMultimap<String, String> headers;

    private List<Predicate<? super T>> filters;

    private Handler<T> deadLetterHandler;

    EventImpl(EventBusImpl eventBus, String name, InetAddress nodeAddress) {
        this.headers = Multimaps.mutable.set.empty();
        this.filters = Lists.mutable.empty();
        this.eventBus = eventBus;
        this.name = name;
        this.node = nodeAddress;
    }

    @Override
    public Event<T> header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public Event<T> header(String key, String... values) {
        Arrays.stream(values)
            .forEach(v -> headers.put(key, v));
        return this;
    }

    @Override
    public Event<T> filter(Predicate<? super T> predicate) {
        filters.add(predicate);
        return this;
    }

    @Override
    public Event<T> deadLetterHandler(Handler<T> handler) {
        this.deadLetterHandler = handler;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Future<Void> purge() {
        return null;
    }

    @Override
    public Future<Void> purge(Handler<?> handler) {
        return null;
    }

    @Override
    public <V> Future<Message<V>> send(T message) {
        Future<Message<V>> future = null; // TODO put implementation here
        String address = UUID.randomUUID().toString();

        eventBus.getEventRouter().registerHandler(address, (Message<V> response) -> {
            if (response.failed()) {
                future.fail(new EventResponseException(response.cause()));
                return;
            }

            future.complete(response);
        });

        return future;
    }

    @Override
    public Future<Void> publish(T message) {
        return null;
    }

    @Override
    public Future<Void> enqueue(T message) {
        return null;
    }

    @Override
    public void consume(Handler<Message<T>> handler) {
        eventBus.getEventRouter().<Message<T>>registerHandler(name, message -> {
            if (filters.isEmpty()) {
                handler.handle(message);
            } else {
                AtomicBoolean shouldExecute = new AtomicBoolean(true);
                filters.forEach(p -> {
                    //noinspection unchecked
                    if (!p.test((T) message))
                        shouldExecute.set(false);
                });

                if (shouldExecute.get()) {
                    handler.handle(message);
                }
            }
        });
    }

    @Override
    public void dequeue(Handler<Message<T>> handler, long interval) {

    }

    @Override
    public Observable<T> observe() {
        return null;
    }
}
