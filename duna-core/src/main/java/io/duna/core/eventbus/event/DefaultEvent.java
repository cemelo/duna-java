package io.duna.core.eventbus.event;

import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.DefaultEventBus;
import io.duna.core.eventbus.Event;
import io.duna.core.eventbus.message.Message;
import io.duna.core.function.Handler;
import io.reactivex.Observable;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class DefaultEvent<T> implements Event<T> {

    private DefaultEventBus eventBus;

    private String name;

    private MutableMultimap<String, String> headers;

    private List<Predicate<? super T>> filters;

    private Handler<T> deadLetterHandler;

    public DefaultEvent(DefaultEventBus eventBus, String name) {
        this.headers = Multimaps.mutable.set.empty();
        this.filters = Lists.mutable.empty();
        this.eventBus = eventBus;
        this.name = name;
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
//        eventBus.getEventRouter().<Message<T>>registerHandler(name, message -> {
//            if (filters.isEmpty()) {
//                handler.handle(message);
//            } else {
//                AtomicBoolean shouldExecute = new AtomicBoolean(true);
//                filters.forEach(p -> {
//                    //noinspection unchecked
//                    if (!p.test((T) message))
//                        shouldExecute.set(false);
//                });
//
//                if (shouldExecute.get()) {
//                    handler.handle(message);
//                }
//            }
//        });
    }

    @Override
    public void dequeue(Handler<Message<T>> handler, long interval) {

    }

    @Override
    public Observable<T> observe() {
        return null;
    }
}
