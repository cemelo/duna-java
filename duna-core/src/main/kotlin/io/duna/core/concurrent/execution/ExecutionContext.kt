package io.duna.core.concurrent.execution

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicBoolean

internal data class ExecutionContext(public  val id: String,
                                     private val executor: ExecutorService,
                                     public  var blocking: Boolean = false) {

  val taskQueue: Queue<() -> Unit> = ConcurrentLinkedQueue()
  val isTaskRunning = AtomicBoolean(false)

  fun execute(task: () -> Unit) {
    if (isTaskRunning.compareAndSet(false, true)) {

    } else {
      taskQueue.offer(task)
    }
  }

  fun execute(task: Runnable) = execute { task.run() }
}
