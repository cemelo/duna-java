package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.reactivex.Flowable
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Represents an event subscriber.
 *
 * _Thread-safety:_ unless otherwise explicitly noted, implementations should not be considered thread-safe.
 *
 * @param T the type of attachments the event messages carry.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 * @see [EventBus]
 */
interface Subscriber<T> : EventProcessor<T> {

  /**
   * Creates a consumer to receive messages from the event bus.
   *
   * This is a terminal operation. This means that, after this operation is called, the subscriber
   * will be registered with the [EventBus] and no changes can be made to its configuration.
   *
   * @param consumer the sink function.
   */
  fun onNext(consumer: Consumer<in Message<T>>): Subscriber<T>

  /**
   * Creates a consumer to receive messages from the event bus.
   *
   * This is a terminal operation. This means that, after this operation is called, the subscriber
   * will be registered with the [EventBus] and no changes can be made to its configuration.
   *
   * @param consumer the sink function.
   */
  fun onNext(consumer: (Message<T>) -> Unit) = onNext(Consumer(consumer::invoke))

  /**
   * Configures a sink to onNext error messages.
   *
   * When the sink is defined, error messages won't be forwarded to the consumer sink registered
   * at the [Subscriber.onNext] function.
   *
   * @param errorSink the sink function.
   * @return this event subscriber
   */
  fun onError(errorSink: Consumer<in Message<T>>): Subscriber<T>

  /**
   * Configures a sink to onNext error messages.
   *
   * When the sink is defined, error messages won't be forwarded to the consumer sink registered
   * at the [Subscriber.onNext] function.
   *
   * @param errorSink the sink function.
   * @return this event subscriber
   */
  fun onError(errorSink: (Message<T>) -> Unit) = onError(Consumer(errorSink::invoke))

  /**
   * Creates a [Flowable] used to observe the event to which this instance is subscribed.
   *
   * This is a terminal operation. This means that, after this operation is called, the subscriber
   * will be registered with the [EventBus] and no changes can be made to its configuration.
   *
   * @return a [Flowable] that observes the subscribed event.
   * @see [Flowable]
   */
  fun toFlowable(): Flowable<T>

  /**
   * Registers a default route to this subscriber in the [EventBus]'s [Router].
   *
   * @return this subscriber.
   */
  fun register(): Subscriber<T>

  /**
   * Removes this subscriber from the [EventBus].
   */
  fun unregister()

  /**
   * FIXME document
   */
  fun accept(message: Message<*>)

  /**
   * Marks this subscriber for execution in a worker pool.
   *
   * This option should be used when the event consumer is used to perform an expensive computation.
   * Since the event bus is executed in an event loop, this prevents any long operations from
   * blocking the main thread.
   *
   * @return this event subscriber
   */
  fun blocking(): Subscriber<T>

  /**
   * Whether this event consumers must run in the worker pool.
   */
  val blocking: Boolean
    get

  override fun filter(predicate: Predicate<in Message<T>>): Subscriber<T>

  override fun filter(predicate: (Message<T>) -> Boolean) = super.filter(predicate) as Subscriber<T>

  override fun intercept(interceptor: Consumer<in Message<T>>): EventProcessor<T>

  override fun intercept(interceptor: (Message<T>) -> Unit) = super.intercept(interceptor) as Subscriber<T>
}
