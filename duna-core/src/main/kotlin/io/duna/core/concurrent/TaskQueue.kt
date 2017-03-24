package io.duna.core.concurrent

import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

internal class TaskQueue(private val executor: ExecutorService) : Runnable {

  private val tasks = ConcurrentLinkedDeque<Pair<ExecutionContext, () -> Unit>>()

  var isShutdown: Boolean = false
    private set

  fun submit(context: ExecutionContext, task: () -> Unit) {
    if (isShutdown) {
      reject()
    }

    tasks.offer(Pair(context, task))
  }

  fun shutdown() {
    isShutdown = true
  }

  override fun run() {
    while (true) {
      if (isShutdown) break

      val (context, task) = takeTask() ?: continue
      context.running = true

      executor
        .submit {
          task.invoke()
          context.running = false
        }
    }
  }

  private fun reject() {
    throw RuntimeException("Rejected")
  }

  private fun takeTask(): Pair<ExecutionContext, () -> Unit>? {
    while (true) {
      val (context, task) = tasks.pollFirst() ?: return null

      if (!context.running)
        return Pair(context, task)

      tasks.offer(Pair(context, task))
    }
  }
}
