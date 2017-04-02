package io.duna.core.bus.event

import io.duna.core.bus.OldEventBus
import io.duna.core.bus.Message
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.util.function.Consumer

/**
 * Represents an event subscriber.
 *
 * @param T the type of attachments the event messages carry.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 * @see [OldEventBus]
 */
interface Subscriber<T> {

  /**
   * The name of the event consumed by this subscriber.
   */
  val event: String

  /**
   * Registers a sink to receive messages from the [OldEventBus].
   *
   * @param messageSink the sink function.
   * @return this subscriber.
   */
  fun onNext(messageSink: Consumer<in Message<T>>): Subscriber<T>

  /**
   * Registers a sink to receive messages from the [OldEventBus].
   *
   * @param messageSink the sink function.
   * @return this subscriber.
   */
  fun onNext(messageSink: (Message<T>) -> Unit) = onNext(Consumer(messageSink::invoke))

  /**
   * Registers a sink to receive error messages from the [OldEventBus].
   *
   * @param errorSink the sink function.
   * @return this subscriber.
   */
  fun onError(errorSink: Consumer<in Message<T>>): Subscriber<T>

  /**
   * Registers a sink to receive error messages from the [OldEventBus].
   *
   * @param errorSink the sink function.
   * @return this subscriber.
   */
  fun onError(errorSink: (Message<T>) -> Unit) = onError(Consumer(errorSink::invoke))

  /**
   * Creates a [Flowable] used to observe the event to which this instance is subscribed.
   *
   * @return a [Flowable] that observes the event subscribed.
   * @see [Flowable]
   */
  fun toFlowable() = toFlowable(BackpressureStrategy.BUFFER)

  /**
   * Creates a [Flowable] used to observe the event to which this instance is subscribed.
   *
   * @param backpressureStrategy the backpressure strategy to be observed by the returned flowable.
   * @return a [Flowable] that observes the event subscribed.
   * @see [Flowable]
   */
  fun toFlowable(backpressureStrategy: BackpressureStrategy): Flowable<T>

  /**
   * Deregisters this subscriber from the [OldEventBus].
   *
   * @return whether the deregistration was successful.
   */
  fun cancel(): Boolean

  /**
   * Marks this subscriber for execution in a worker pool.
   *
   * This option should be used when the consumer functions perform expensive computations.
   * As the [OldEventBus] is executed in an event loop, this prevents any long operations from
   * blocking the main thread.
   *
   * @return this event subscriber
   */
  fun blocking(): Subscriber<T>

  /**
   * Whether this subscriber should run in the worker pool.
   */
  val isBlocking: Boolean

}
