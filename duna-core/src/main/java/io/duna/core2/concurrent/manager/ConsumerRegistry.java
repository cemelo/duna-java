package io.duna.core2.concurrent.manager;

import io.duna.core2.Context;
import io.duna.core2.ContextImpl;
import io.duna.core2.Duna;
import io.duna.core2.DunaImpl;
import io.duna.core2.function.Consumer;
import io.netty.channel.EventLoopGroup;
import kotlin.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

public class ConsumerRegistry {

    private final DunaImpl manager;
    private final Map<Consumer<?>, ExecutorService> consumerExecutors;

    private final EventLoopGroup eventExecutors;
    private final ExecutorService workerPool;

    final NavigableSet<Pair<Integer, ExecutorService>> executorsQueue;

    public ConsumerRegistry(DunaImpl manager, EventLoopGroup eventExecutors, ExecutorService workerPool) {
        this.manager = manager;
        this.eventExecutors = eventExecutors;
        this.workerPool = workerPool;
        this.consumerExecutors = new ConcurrentHashMap<>();

        this.executorsQueue = new ConcurrentSkipListSet<>((p1, p2) -> {
            if (p1.getSecond() == p2.getSecond()) return 0;
            else if (p1.getFirst().equals(p2.getFirst())) return 1;

            return p1.getFirst() - p2.getFirst();
        });

        eventExecutors.forEach(el -> this.executorsQueue.add(new Pair<>(0, el)));
    }

    public void register(Consumer<?> consumer, ConsumerOptions options) {
        if (consumerExecutors.containsKey(consumer))
            throw new IllegalArgumentException("Consumer already registered.");

        consumerExecutors.put(consumer,
            options.isBlocking() ? workerPool : nextExecutor(options.getCost()));
    }

    public void purge(Consumer<?> consumer, ConsumerOptions options) {
        ExecutorService removed = consumerExecutors.remove(consumer);

        Iterator<Pair<Integer, ExecutorService>> it = executorsQueue.iterator();
        while (it.hasNext()) {
            Pair<Integer, ExecutorService> p = it.next();
            if (p.getSecond() == removed) {
                it.remove();
                executorsQueue.add(new Pair<>(p.getFirst() - options.getCost(), removed));
                break;
            }
        }
    }

    private <T> void execute(Consumer<T> consumer, T offer) {
        final ExecutorService executor = consumerExecutors.get(consumer);
        final Context context = Duna.context().orElseGet(() -> new ContextImpl(manager));

        executor.execute(() -> {
            DunaImpl.setContextHolder(context);
            consumer.accept(offer);
        });
    }

    private ExecutorService nextExecutor(int cost) {
        Pair<Integer, ExecutorService> executorPair = executorsQueue.pollFirst();
        executorsQueue.add(new Pair<>(executorPair.getFirst() + cost, executorPair.getSecond()));

        return executorPair.getSecond();
    }
}

