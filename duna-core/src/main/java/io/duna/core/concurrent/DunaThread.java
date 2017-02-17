package io.duna.core.concurrent;

import io.netty.channel.EventLoop;
import org.jetbrains.annotations.NonNls;

public class DunaThread extends Thread {

    private EventLoop eventLoop;

    public DunaThread(Runnable runnable, @NonNls String s) {
        super(runnable, s);
    }

    public EventLoop getEventLoop() {
        return eventLoop;
    }

    void setEventLoop(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }
}
