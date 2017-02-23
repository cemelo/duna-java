package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.reactivex.Flowable
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Represents an event subscriber.
 *
 * @author [Carlos Eduardo Melo][mail@cemelo.com]
 * @see [EventBus]
 */
interface Subscriber<T> : Event<T> {

  /**
   * Marks this subscriber for execution in a worker pool.
   *
   * @return this subscriber
   */
  fun blocking(): Subscriber<T>

  fun withErrorSink(errorSink: Consumer<in Message<T>>): Subscriber<T>

  override fun filter(predicate: Predicate<in Message<T>>): Subscriber<T>

  override fun intercept(consumer: Consumer<in Message<T>>): Subscriber<T>

  fun thenConsume(consumer: Consumer<in Message<T>>): Subscriber<T>

  fun toFlowable(): Flowable<T>

  fun clear(): Unit

}
