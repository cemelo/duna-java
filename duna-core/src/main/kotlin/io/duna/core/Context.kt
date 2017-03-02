package io.duna.core

/**
 * Represents the execution context of an event.
 *
 * Since there is only one context object per thread of execution, there is
 * no need for synchronization.
 */
interface Context : MutableMap<String, Any> {

  /**
   * @return this context manager.
   */
  fun manager(): Manager

  /**
   * Used to hold the [Context] instance associated to the current thread.
   */
  companion object {

    private var context: ThreadLocal<Context?> = ThreadLocal()

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
