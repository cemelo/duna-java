package io.duna.core.eventbus.event

import io.duna.core.eventbus.Message
import java.util.function.Consumer
import java.util.function.Predicate

interface Event<T> {

  /**
   * Filters messages produced or received by this event.
   *
   * @param predicate the predicate used to test messages.
   * @return this event
   */
  fun filter(predicate: Predicate<in Message<T>>): Event<T>

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
  fun intercept(interceptor: Consumer<in Message<T>>): Event<T>

  /**
   * Intercepts any messages produced or received by this event.
   * @return this event
   */
  fun intercept(interceptor: (Message<T>) -> Unit) = intercept(Consumer(interceptor::invoke))

  val name: String
    /**
     * @return this event's name.
     */
    get(): String = name

}
