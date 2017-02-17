package io.duna.core.concurrent;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;

import java.util.concurrent.Executor;

public class DunaEventLoop extends DefaultEventLoop {

    public DunaEventLoop(EventLoopGroup parent, Executor executor) {
        super(parent, executor);
    }


    @Override
    protected void run() {
        if (!(Thread.currentThread() instanceof DunaThread))
            throw new IllegalStateException("Event loop isn't running on a duna thread.");

        DunaThread t = (DunaThread) Thread.currentThread();
        t.setEventLoop(this);

        super.run();
    }
}
