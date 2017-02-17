package io.duna.core.concurrent;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;

import java.util.concurrent.Executor;

public class DunaEventLoopGroup extends DefaultEventLoopGroup {

    public DunaEventLoopGroup(int nThreads) {
        super(nThreads, new DunaThreadFactory());
    }

    @Override
    protected EventLoop newChild(Executor executor, Object... args) throws Exception {
        return new DunaEventLoop(this, executor);
    }
}
