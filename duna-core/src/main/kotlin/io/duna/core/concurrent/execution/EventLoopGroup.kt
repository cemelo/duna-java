package io.duna.core.concurrent.execution

import io.netty.util.concurrent.EventExecutorGroup

interface EventLoopGroup : EventExecutorGroup {

  override fun next(): EventLoop
}
