package io.duna.core.concurrent.execution

import io.netty.util.concurrent.EventExecutor

interface EventLoop : EventExecutor {

  val parent: EventLoopGroup
}
