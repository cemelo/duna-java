package io.duna.core.concurrent

import java.util.function.Consumer

interface Future<T> {
  fun done(): Boolean
  fun completed(): Boolean
  fun failed(): Boolean
  fun cancelled(): Boolean

  fun onComplete(consumer: Consumer<T>): Future<T>
  fun onError(consumer: Consumer<Throwable>): Future<T>
  fun onCancel(consumer: Consumer<Void>): Future<T>

  fun cancel()

  fun complete() {
    complete(null)
  }

  fun complete(result: T?)

  fun fail(error: Throwable)

  companion object {
    fun completedFuture() = null
  }
}
