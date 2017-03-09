package io.duna.core.eventbus

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.event.Event
import io.duna.core.eventbus.event.Subscriber
import io.duna.core.eventbus.queue.MessageQueue
import io.duna.core.eventbus.routing.Router

/**
 * The event bus.
 *
 * The event bus is responsible for creating event emitters and registering
 * event subscribers.
 *
 * @author [Carlos Eduardo Melo][ceduardo.melo@gmail.com]
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
   * @param event the event of the event to be consumed.
   * @return the event subscriber.
   */
  fun <T> subscriber(event: String): Subscriber<T>

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

  /**
   * Returns the event bus router.
   */
  fun router(): Router

  fun accept(message: Message<*>)

  fun dispatch(message: Message<*>): Future<Message<*>>

  fun purge(subscriber: Subscriber<*>): Boolean
}

