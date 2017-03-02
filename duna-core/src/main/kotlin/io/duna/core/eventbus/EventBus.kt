package io.duna.core.eventbus

import io.duna.core.eventbus.event.Emitter
import io.duna.core.eventbus.event.Subscriber
import io.duna.core.eventbus.queue.MessageQueue
import io.duna.core.eventbus.routing.Router
import java.util.function.Consumer
import java.util.function.Predicate

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
   * Creates an event emitter.
   *
   * @param event the name of the event to be emitted.
   * @return the event emitter.
   */
  fun <T> emitter(event: String): Emitter<T>

  /**
   * Creates an event subscriber.
   *
   * @param event the name of the event to be consumed.
   * @return the event subscriber.
   */
  fun <T> subscriber(event: String): Subscriber<T>

  /**
   * Creates a message queue.
   *
   * This queue provides items to be polled by other members
   * of the event bus.
   *
   * @param name the queue name
   * @return the message queue.
   */
  fun <T> queue(name: String): MessageQueue<T>

  /**
   * Returns the event bus pipeline.
   */
  fun pipeline(): Pipeline

  /**
   * Returns the event bus router.
   */
  fun router(): Router

  /**
   * Creates a global filter for messages received by the event bus. Any
   * messages not matching the predicate provided will be discarded.
   */
  fun filter(filter: Predicate<Message<*>>): EventBus

  /**
   * Creates a global interceptor of messages received by the event bus.
   */
  fun intercept(interceptor: Consumer<Message<*>>): EventBus

  fun accept(message: Message<*>)
}

