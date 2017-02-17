package io.duna.core.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.WeakHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DunaThreadFactory implements ThreadFactory {

    private final AtomicInteger threadId;
    private String threadPrefix;

    private WeakHashMap<DunaThread, Void> threads;

    public DunaThreadFactory() {
        this("duna");
    }

    public DunaThreadFactory(String threadPrefix) {
        this.threadId = new AtomicInteger(0);
        this.threads = new WeakHashMap<>();
        this.threadPrefix = threadPrefix;
    }

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        DunaThread newInstance = new DunaThread(runnable, threadPrefix + "-" + threadId.getAndIncrement());
        threads.put(newInstance, null);

        return newInstance;
    }
}
