package io.duna.core.concurrent.execution

import io.netty.util.concurrent.SingleThreadEventExecutor
import java.util.concurrent.Executor

/**
 */
class DefaultEventLoop(override val parent: EventLoopGroup,
                       executor: Executor)
  : EventLoop, SingleThreadEventExecutor(parent, executor, true) {

  private val tailTasks = newTaskQueue(DEFAULT_MAX_PENDING_TASKS)

  fun executeOnNextIteration(task: Runnable) {
    if (isShutdown) reject()

    if (!tailTasks.offer(task))
      reject(task)

    if (wakesUpForTask(task))
      wakeup(inEventLoop())
  }

  override fun wakesUpForTask(task: Runnable): Boolean {
    return super.wakesUpForTask(task)
  }

  override fun afterRunningAllTasks() {
    runAllTasksFrom(tailTasks)
  }

  override fun hasTasks(): Boolean {
    return super.hasTasks() || tailTasks.isNotEmpty()
  }

  override fun pendingTasks(): Int {
    return super.pendingTasks() + tailTasks.size
  }

  override fun run() {
    while (true) {
      val task = takeTask()
      if (task != null) {
        task.run()
        updateLastExecutionTime()
      }

      if (confirmShutdown()) {
        break
      }
    }
  }

  companion object {
    val DEFAULT_MAX_PENDING_TASKS = 16
  }
}
