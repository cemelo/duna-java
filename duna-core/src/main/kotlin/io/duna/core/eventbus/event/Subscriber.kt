package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.reactivex.Flowable
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Represents an event subscriber.
 *
 * @param T the type of attachments the event messages carry.
 *
 * @author [Carlos Eduardo Melo][mail@cemelo.com]
 * @see [EventBus]
 */
interface Subscriber<T> : Event<T> {

  /**
   * Creates a consumer to receive messages from the event bus.
   *
   * This is a terminal operation. This means that, after this operation is called, the subscriber
   * will be registered with the [EventBus] and no changes can be made to its configuration.
   *
   * @param consumer the sink function.
   */
  fun consume(consumer: Consumer<in Message<T>>): Unit

  /**
   * Creates a hot [Flowable] used to observe the event to which this instance is subscribed.
   *
   * This is a terminal operation. This means that, after this operation is called, the subscriber
   * will be registered with the [EventBus] and no changes can be made to its configuration.
   *
   * @return a hot [Flowable] that observes the subscribed event.
   * @see [Flowable]
   */
  fun toFlowable(): Flowable<T>

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
   * Configures a sink to consume error messages.
   *
   * When the sink is defined, error messages won't be forwarded to the consumer sink registered
   * at the [Subscriber.consume] function.
   *
   * @param errorSink the sink function.
   * @return this event subscriber
   */
  fun withErrorSink(errorSink: Consumer<in Message<Throwable>>): Subscriber<T>

  override fun filter(predicate: Predicate<in Message<T>>): Subscriber<T>

  override fun intercept(consumer: Consumer<in Message<T>>): Subscriber<T>

}
