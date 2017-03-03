package io.duna.core.concurrent

import io.duna.core.concurrent.FutureState.*
import java.util.concurrent.atomic.AtomicReference

internal abstract class AbstractFuture<T> : Future<T> {

  private val state = AtomicReference<FutureState>(NEW)

  override fun done() = state.get() != NEW

  override fun completed() = state.get() == COMPLETED

  override fun failed() = state.get() == FAILED

  override fun cancelled() = state.get() == CANCELLED

  override fun cancel() {
    if (!state.compareAndSet(NEW, CANCELLED))
      throw IllegalStateException()
  }

  override fun complete(result: T?) {
    if (!state.compareAndSet(NEW, COMPLETED))
      throw IllegalStateException()
  }

  override fun fail(error: Throwable) {
    if (!state.compareAndSet(NEW, FAILED))
      throw IllegalStateException()
  }
}
