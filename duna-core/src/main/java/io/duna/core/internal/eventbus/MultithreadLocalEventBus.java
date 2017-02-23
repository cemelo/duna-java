package io.duna.core.internal.eventbus;

import io.duna.core.Context;
import io.duna.core.concurrent.Future;
import io.duna.core.eventbus.EventBus;
import io.duna.core.eventbus.EventRouter;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.Emitter;
import io.duna.core.eventbus.event.Subscriber;
import io.duna.core.eventbus.queue.InvalidQueueException;
import io.duna.core.eventbus.queue.MessageQueue;
import io.duna.core.internal.ContextImpl;
import io.duna.core.internal.eventbus.event.DefaultInboundEvent;
import io.duna.core.internal.eventbus.event.DefaultOutboundEvent;
import io.duna.core.util.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.logging.Logger;


public class MultithreadLocalEventBus implements EventBus {

    private static final Logger log = Logger.getLogger(MultithreadLocalEventBus.class.getName());

    private final Duna manager;
    private final EventRouter router;

    private final Map<String, ExecutorService> eventExecutors;
    private final ExecutorService workerExecutor;

    private final NavigableSet<Pair<Integer, ExecutorService>> executorsQueue;

    private Consumer<Message<?>> defaultDeadLetterSink;

    public MultithreadLocalEventBus(@NotNull Duna manager,
                                    @NotNull ExecutorService eventExecutors,
                                    @NotNull ExecutorService workerExecutor) {
        this(manager, Collections.singletonList(eventExecutors), workerExecutor);
    }

    public MultithreadLocalEventBus(@NotNull Duna manager,
                                    @NotNull Iterable<ExecutorService> eventExecutors,
                                    @NotNull ExecutorService workerExecutor) {
        this.manager = manager;
        this.router = new LocalEventRouter();

        this.eventExecutors = new ConcurrentHashMap<>();
        this.workerExecutor = workerExecutor;

        this.executorsQueue = new ConcurrentSkipListSet<>((p1, p2) -> {
            if (p1.getSecond() == p2.getSecond()) return 0;
            else if (p1.getFirst().equals(p2.getFirst())) return 1;

            return p1.getFirst() - p2.getFirst();
        });

        eventExecutors.forEach(el -> this.executorsQueue.add(new Pair<>(0, el)));
    }

    @Override
    public void setDefaultDeadLetterSink(Consumer<Message<?>> messageHandler) {
        this.defaultDeadLetterSink = messageHandler;
    }

    @Override
    public <T> Emitter<T> outbound(String name) {
        return new DefaultOutboundEvent<>(this, name);
    }

    @Override
    public <T> Subscriber<T> inbound() {
        return new DefaultInboundEvent<>(this, UUID.randomUUID().toString());
    }

    @Override
    public <T> Subscriber<T> inbound(String name) {
        return new DefaultInboundEvent<>(this, name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void queue(String name, MessageQueue<T> producer) {
        log.fine(() -> name + ": registering queue producer");

        this.<T>inbound(name)
            .setBlocking(producer.isBlocking())
            .addListener(m -> {
                Message<T> message;

                if (producer.isClosed()) {
                    message = SimpleMessage.<T>builder()
                        .source(name)
                        .target(m.getRespondTo())
                        .cause(new InvalidQueueException())
                        .headers(m.getHeaders())
                        .parent(this)
                        .build();

                    this.cancel(name);
                } else {
                    message = SimpleMessage.<T>builder()
                        .source(name)
                        .target(m.getRespondTo())
                        .body(producer.poll())
                        .headers(m.getHeaders())
                        .parent(this)
                        .build();
                }

                dispatch(message, false, (Consumer) defaultDeadLetterSink);
            });
    }

    public <T> void poll(String queue, Subscriber<T> targetEvent) {
        this.outbound(queue)
            .send(targetEvent);
    }

    @Override
    public <T> Future<Void> dispatch(Message<T> outgoing, boolean multicast, Consumer<Message<T>> deliveryErrorConsumer) {
        log.fine(() -> outgoing.getTarget() + ": dispatching message to event");

        process(outgoing);
        return Future.completedFuture();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void process(Message<T> incoming) {
        if (!router.contains(incoming.getTarget())) {
            log.fine(() -> incoming.getTarget() + ": no events registered under this name");
            return;
        }

        Subscriber<?> event = router.get(incoming.getTarget());

        Context context = new ContextImpl(manager);
        context.put("current-event", event);

        Runnable code = () -> {
            ContextImpl.setContext(context);
            ((Subscriber) event).accept(incoming);
            ContextImpl.setContext(null);
        };

        if (event.isBlocking()) {
            log.fine(() -> event.getName() + ": Executing event consumer in the worker pool");
            workerExecutor.execute(code);
        } else {
            log.fine(() -> event.getName() + ": Executing event consumer in the consumer pool");
            eventExecutors
                .get(incoming.getTarget())
                .execute(code);
        }
    }

    public boolean register(Subscriber<?> event) {
        if (router.contains(event.getName()))
            return false;

        eventExecutors.put(event.getName(), nextExecutor());
        router.register(event);

        return true;
    }

    public void cancel(String event) {
        router.cancel(event);

        decreaseExecutorUsage(eventExecutors.get(event));
        eventExecutors.remove(event);
    }

    public void cancel(Subscriber<?> event) {
        cancel(event.getName());
    }

    private ExecutorService nextExecutor() {
        Pair<Integer, ExecutorService> executorPair = executorsQueue.pollFirst();
        executorsQueue.add(new Pair<>(executorPair.getFirst() + 1, executorPair.getSecond()));

        return executorPair.getSecond();
    }

    private void decreaseExecutorUsage(ExecutorService executorService) {
        Iterator<Pair<Integer, ExecutorService>> it = executorsQueue.iterator();
        while (it.hasNext()) {
            Pair<Integer, ExecutorService> p = it.next();
            if (p.getSecond() == executorService) {
                it.remove();
                executorsQueue.add(new Pair<>(p.getFirst() - 1, executorService));
                break;
            }
        }
    }
}
