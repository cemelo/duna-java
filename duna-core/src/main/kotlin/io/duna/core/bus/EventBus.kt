package io.duna.core.bus

import io.duna.core.concurrent.Future
import io.duna.core.bus.event.Event
import io.duna.core.bus.event.Subscriber
import io.duna.core.bus.queue.MessageQueue
import io.duna.core.bus.routing.Router

/**
 * The event bus.
 *
 * The event bus is responsible for creating event emitters and registering
 * event subscribers.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 */
interface EventBus {

  /**
   * Prepares an event to be emitted.
   *
   * @param name the event of the event to be emitted.
   * @return a fluent API responsible for emitting an event.
   */
  fun <T> emit(name: String): Event<T>

  /**
   * Creates an event subscriber.
   *
   * @param toEvent the name of the event to be consumed.
   * @return the event subscriber.
   */
  fun <T> subscribe(toEvent: String): Subscriber<T>

  /**
   * Creates a message queue.
   *
   * This queue provides items to be polled by other members
   * of the event bus.
   *
   * @param name the queue event
   * @return the message queue.
   */
  fun <T> queue(name: String): MessageQueue<T>

  fun accept(message: Message<Any>)

  fun dispatch(message: Message<Any>): Future<Message<Any>>

  fun expunge(subscriber: Subscriber<*>): Boolean

}

