package io.duna.core.concurrent

import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

internal class FutureImpl<T> : AbstractFuture<T>() {

  private @Volatile var result: T? = null
  private @Volatile var error: Throwable? = null

  private var completionConsumer: Consumer<T?>? = null
  private var errorConsumer: Consumer<Throwable>? = null
  private var cancellationConsumer: Consumer<Unit>? = null

  private val consumerInvoked = AtomicBoolean(false)

  override fun cancel() {
    super.cancel()

    if (cancellationConsumer != null && consumerInvoked.compareAndSet(false, true))
      cancellationConsumer?.accept(Unit)
  }

  override fun complete(result: T?) {
    super.complete(result)

    if (completionConsumer != null && consumerInvoked.compareAndSet(false, true))
      completionConsumer?.accept(result)
  }

  override fun fail(error: Throwable) {
    super.fail(error)

    if (errorConsumer != null && consumerInvoked.compareAndSet(false, true))
      errorConsumer?.accept(error)
  }

  override fun onComplete(consumer: Consumer<T?>): Future<T> {
    this.completionConsumer = consumer

    if (completed() && consumerInvoked.compareAndSet(false, true))
      consumer.accept(result)

    return this
  }

  override fun onError(consumer: Consumer<Throwable>): Future<T> {
    this.errorConsumer = consumer

    if (failed() && consumerInvoked.compareAndSet(false, true))
      consumer.accept(error ?: RuntimeException())

    return this
  }

  override fun onCancel(consumer: Consumer<Unit>): Future<T> {
    this.cancellationConsumer = consumer

    if (cancelled() && consumerInvoked.compareAndSet(false, true))
      consumer.accept(Unit)

    return this
  }
}
