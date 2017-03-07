package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Represents an event emitter or subscriber.
 *
 * _Thread-safety:_ unless otherwise explicitly noted, implementations should not be considered thread-safe.
 *
 * @param T the type of attachment the event messages must carry.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 * @see [EventBus]
 */
interface EventProcessor<T> {

  /**
   * Filters messages produced or received by this event.
   *
   * @param predicate the predicate used to test messages.
   * @return this event
   */
  fun filter(predicate: Predicate<in Message<T>>): EventProcessor<T>

  /**
   * Filters messages produced or received by this event.
   *
   * @param predicate the predicate used to test messages.
   * @return this event
   */
  fun filter(predicate: (Message<T>) -> Boolean) = filter(Predicate(predicate::invoke))

  /**
   * Intercepts any messages produced or received by this event.
   * @return this event
   */
  fun intercept(interceptor: Consumer<in Message<T>>): EventProcessor<T>

  /**
   * Intercepts any messages produced or received by this event.
   * @return this event
   */
  fun intercept(interceptor: (Message<T>) -> Unit): EventProcessor<T> = intercept(Consumer(interceptor::invoke))

  /**
   * This event's name.
   */
  val name: String
    /**
     * @return this event's name.
     */
    get() = name

}
