package io.duna.core.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DunaThreadPerTaskExecutor implements Executor {

    private final ThreadFactory threadFactory;

    public DunaThreadPerTaskExecutor() {
        this.threadFactory = new DunaThreadFactory();
    }

    @Override
    public void execute(@NotNull Runnable runnable) {
        DunaThread thread = (DunaThread) threadFactory.newThread(runnable);
        thread.start();
    }
}
