package io.duna.core.concurrent

import java.util.function.Consumer

/**
 * Represents an asynchronous computation.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 */
interface Future<T> {

  /**
   * @return whether the asynchronous computation this future represents is terminated.
   */
  fun done(): Boolean

  /**
   * @return whether the computation completed successfully or not.
   */
  fun completed(): Boolean

  /**
   * @return whether there was an error while performing the computation.
   */
  fun failed(): Boolean

  /**
   * @return whether the asynchronous computation was cancelled.
   */
  fun cancelled(): Boolean

  /**
   * Registers a consumer to be invoked when the underlying computation completes successfully.
   *
   * @param consumer the completion consumer.
   * @return this future
   */
  fun onComplete(consumer: Consumer<T?>): Future<T>

  /**
   * Registers a consumer to be invoked when the underlying computation completes successfully.
   *
   * @param consumer the completion consumer.
   * @return this future
   */
  fun onComplete(consumer: (T?) -> Unit) = onComplete(Consumer(consumer::invoke))

  /**
   * Registers a consumer to be invoked when there is an error while performing the computation.
   *
   * @param consumer the error consumer
   * @return this future
   */
  fun onError(consumer: Consumer<Throwable>): Future<T>

  /**
   * Registers a consumer to be invoked when there is an error while performing the computation.
   *
   * @param consumer the error consumer
   * @return this future
   */
  fun onError(consumer: (Throwable) -> Unit) = onError(Consumer(consumer::invoke))

  /**
   * Registers a consumer to be invoked when the computation is cancelled.
   *
   * @param consumer the cancellation consumer.
   * @return this future
   */
  fun onCancel(consumer: Consumer<Unit>): Future<T>

  /**
   * Registers a consumer to be invoked when the computation is cancelled.
   *
   * @param consumer the cancellation consumer.
   * @return this future
   */
  fun onCancel(consumer: () -> Unit) = onCancel(Consumer { consumer.invoke() })

  /**
   * Cancels the underlying computation.
   */
  fun cancel()

  /**
   * Completes this future without any results.
   */
  fun complete() {
    complete(null)
  }

  /**
   * Completes this future with a result.
   *
   * @param result the asynchronous computation result.
   */
  fun complete(result: T?)

  /**
   * Terminates this future with an error.
   *
   * @param error the error thrown by the asynchronous computation.
   */
  fun fail(error: Throwable)

  companion object {

    /**
     * Returns a [Future] instance already completed.
     *
     * @return a completed future.
     */
    fun <T> completedFuture(): Future<T> {
      val future = FutureImpl<T>()
      future.complete()

      return future
    }
  }
}
