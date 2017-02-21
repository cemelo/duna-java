package io.duna.core.internal.eventbus;

import io.duna.core.Context;
import io.duna.core.Duna;
import io.duna.core.concurrent.future.Future;
import io.duna.core.eventbus.EventBus;
import io.duna.core.eventbus.EventRouter;
import io.duna.core.eventbus.Message;
import io.duna.core.eventbus.event.Event;
import io.duna.core.eventbus.event.InboundEvent;
import io.duna.core.eventbus.event.OutboundEvent;
import io.duna.core.internal.ContextImpl;
import io.duna.core.internal.eventbus.event.DefaultInboundEvent;
import io.duna.core.internal.eventbus.event.DefaultOutboundEvent;
import io.duna.core.util.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class LocalEventBus implements EventBus {

    private final Duna manager;
    private final EventRouter router;

    private final Map<String, ExecutorService> eventExecutors;
    private final ExecutorService workerExecutor;

    private final NavigableSet<Pair<Integer, ExecutorService>> executorsQueue;

    public LocalEventBus(@NotNull Duna manager,
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
    public <T> OutboundEvent<T> outbound(String name) {
        return new DefaultOutboundEvent<>(this, name);
    }

    @Override
    public <T> OutboundEvent<T> outbound(String name, int cost) {
        return new DefaultOutboundEvent<T>(this, name).setCost(cost);
    }

    @Override
    public <T> InboundEvent<T> inbound() {
        return new DefaultInboundEvent<>(this, UUID.randomUUID().toString());
    }

    @Override
    public <T> InboundEvent<T> inbound(String name) {
        return new DefaultInboundEvent<>(this, name);
    }

    @Override
    public <T> InboundEvent<T> inbound(String name, int cost) {
        return new DefaultInboundEvent<T>(this, name).setCost(cost);
    }

    @Override
    public <T> Future<Void> dispatch(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer) {
        process(outgoing); // Dispatches to the local eventbus
        return Future.completedFuture();
    }

    @Override
    public <T> Future<Void> publish(Message<T> outgoing, Consumer<Message<T>> deliveryErrorConsumer) {

        /* The implementation here is the same as `dispatch` because you can only have
         * one event registered per eventbus node. In distributed environments,
         * this should broadcast the message to all nodes in the cluster.
         */
        process(outgoing);
        return Future.completedFuture();
    }

    @Override
    public <T> void process(Message<T> incoming) {
        if (!router.contains(incoming.getTarget())) {
            // The event isn't registered with this eventbus node
            return;
        }

        // Process the event in the thread corresponding to the event
        Event<?> inbound = router.get(incoming.getTarget());

        if (inbound instanceof DefaultInboundEvent) {
            Context context = new ContextImpl(manager);
            context.put("current-event", inbound);

            if (((DefaultInboundEvent) inbound).isBlocking()) {
                // TODO treat the problem of ThreadLocal corruption with ForkJoinPools
                workerExecutor.execute(() -> {
                    ContextImpl.setContext(context);
                    ((DefaultInboundEvent<T>) inbound).accept(incoming);
                    ContextImpl.setContext(null);
                });
            } else {
                // Processes the message using the assigned event executor
                eventExecutors.get(incoming.getTarget()).execute(() -> {
                    ContextImpl.setContext(context);
                    ((DefaultInboundEvent<T>) inbound).accept(incoming);
                    ContextImpl.setContext(null);
                });
            }
        }
    }

    public boolean register(Event<?> event) {
        if (router.contains(event.getName()))
            return false;

        eventExecutors.put(event.getName(), nextExecutor(event.getCost()));
        router.register(event);

        return true;
    }

    public void cancel(Event<?> event) {
        router.cancel(event);
        decreaseExecutorUsage(eventExecutors.get(event.getName()), event.getCost());
    }

    public <T> void enqueue(String queue, T item) {

    }

    public <T> T poll(String queue, Event<?> targetEvent) {
        return null;
    }

    private ExecutorService nextExecutor(int cost) {
        if (cost < 0)
            throw new IllegalArgumentException("The event cost must be a positive integer.");

        Pair<Integer, ExecutorService> executorPair = executorsQueue.pollFirst();
        executorsQueue.add(new Pair<>(executorPair.getFirst() + cost + 1, executorPair.getSecond()));

        return executorPair.getSecond();
    }

    private void decreaseExecutorUsage(ExecutorService executorService, int cost) {
        Iterator<Pair<Integer, ExecutorService>> it = executorsQueue.iterator();
        while (it.hasNext()) {
            Pair<Integer, ExecutorService> p = it.next();
            if (p.getSecond() == executorService) {
                it.remove();
                executorsQueue.add(new Pair<>(p.getFirst() - cost - 1, executorService));
                break;
            }
        }
    }
}
