package io.duna.core.concurrent.execution

import io.netty.util.concurrent.EventExecutor
import io.netty.util.concurrent.MultithreadEventExecutorGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadFactory

class DefaultEventLoopGroup(nThreads: Int,
                            executor: Executor,
                            private val workerPool: ExecutorService,
                            vararg args: Any?)
  : EventLoopGroup, MultithreadEventExecutorGroup(nThreads, executor, *args) {

  private val registeredContexts = ConcurrentHashMap<String, EventLoop>()

  internal fun createExecutionContext(event: String): ExecutionContext {
//    val eventLoop = next()
//    val context = TaskContext(event, eventLoop, workerPool)
//
//    registeredContexts[event] = eventLoop
//
//    return context
    TODO()
  }

  internal fun removeExecutionContext(event: String) {
    registeredContexts.remove(event)
  }

  override fun next(): EventLoop {
    return super.next() as EventLoop
  }

  override fun newDefaultThreadFactory(): ThreadFactory {
    return DunaThreadFactory()
  }

  override fun newChild(executor: Executor, vararg args: Any?): EventExecutor {
    return DefaultEventLoop(this, executor)
  }
}
