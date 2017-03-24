package io.duna.core

import io.duna.core.bus.event.Subscriber
import io.duna.core.concurrent.execution.DunaThread

/**
 * Represents the execution context of an event.
 *
 * Since there is only one context object per thread of execution, there is
 * no need for synchronization.
 */
interface Context : MutableMap<String, Any> {

  fun execute(block: () -> Unit)

  fun executeBlocking(block: () -> Unit)

  /**
   * Used to hold the [Context] instance associated to the current thread.
   */
  companion object {

    private var context: ThreadLocal<Context?> = ThreadLocal()

    val inDunaThread: Boolean
      get() = Thread.currentThread() is DunaThread

    /**
     * @return the context associated to the current thread.
     */
    fun currentContext(): Context? {
      return context.get()
    }

    internal fun setContext(context: Context?) =
      this.context.set(context)
  }
}
