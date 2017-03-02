package io.duna.core.eventbus.queue

import java.util.function.BiFunction
import java.util.function.Function

/**
 * Representes a message queue.
 *
 * @author [Carlos Eduardo Melo][ceduardo.melo@gmail.com]
 */
interface MessageQueue<T> {

  /**
   * Requests an item from this queue.
   *
   * @return the first item available in this queue.
   */
  fun poll(): T

  /**
   * Offers an item to this queue.
   *
   * @param item the item offered.
   * @return whether the queue accepted the offered item or not.
   */
  fun offer(item: T): Boolean

  /**
   * Closes this queue.
   */
  fun close()

  /**
   * @return whether the queue is closed or not.
   */
  fun isClosed(): Boolean

  /**
   * @return wheter the consumer/producer functions are blocking and, thus,
   *         should be executed in the worker pool.
   */
  fun isBlocking(): Boolean

  /**
   * Sets the producer of items of this queue.
   *
   * @param producer a function responsible for producing items.
   * @return this queue.
   */
  fun setProducer(producer: QueueConsumer<T>): MessageQueue<T>

  /**
   * Sets the consumer of items offered to this queue.
   *
   * @param consumer a function responsible for consuming items.
   * @return this queue.
   */
  fun setConsumer(consumer: QueueConsumer<T>): MessageQueue<T>

  /**
   * Sets whether the consumer/producer functions are blocking.
   * @return this queue.
   */
  fun setBlocking(blocking: Boolean): MessageQueue<T>
}

/**
 * An item producer function to be used in [MessageQueue]s.
 *
 * There is no requirement as whether the resulting items should be
 * distinct or not through each invocation.
 *
 * @param T the type of items produced by the queue.
 * @return an item produced by the queue.
 */
typealias QueueProducer<T> = Function<in MessageQueue<in T>, out T>

/**
 * An item consumer function to be used in [MessageQueue]s.
 *
 * @param T the type of items consumed by the queue.
 * @return whether the item was consumed or not.
 */
typealias QueueConsumer<T> = BiFunction<in MessageQueue<in T>, in T, Boolean>
