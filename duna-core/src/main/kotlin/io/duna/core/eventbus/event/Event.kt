package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
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
   * Intercepts any messages produced or received by this event.
   * @return this event
   */
  fun intercept(consumer: Consumer<in Message<T>>): Event<T>

  /**
   * Registers this event with the [EventBus].
   */
  fun register(): Unit

  /**
   * Purges this event from the [EventBus].
   */
  fun purge(): Unit

  /**
   * @return this event's name.
   */
  fun getName(): String

}
