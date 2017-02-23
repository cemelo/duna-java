package io.duna.core.eventbus.event

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import org.eclipse.collections.api.multimap.Multimap
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Represents an event emitter.
 *
 * @author [Carlos Eduardo Melo][mail@cemelo.com]
 * @see [EventBus]
 */
interface Emitter<T> : Event<T> {

  /**
   * Emits this event a single node. If there is more than subscriber to this event,
   * the [EventBus] will select one to consume it according to the ESB configuration.
   *
   * @param attachment the attachment carried by this event.
   * @return a future to be completed when the message is sent by the local [EventBus] node.
   */
  fun emit(attachment: T): Future<Emitter<T>>

  /**
   * Emits this event to all subscribers.
   *
   * @param attachment the attachment carried by this event.
   * @return a future to be completed when the message is sent by the local [EventBus] node.
   */
  fun publish(attachment: T): Future<Emitter<T>>

  /**
   * Emits this event and initiates a subscriber to receive a response. If there is more than subscriber
   * to this event, the [EventBus] will select one to consume it according to the ESB configuration.
   *
   * @param attachment  the attachment carried by this event.
   * @param V           the type of the attachment carried by the response event.
   * @return the unregistered subscriber to the response event.
   */
  fun <V> request(attachment: T): Subscriber<V>

  /**
   * Emits this event to all subscribers and initiates a subscriber to receive the responses.
   *
   * @param attachment  the attachment carried by this event.
   * @param V           the type of the attachment carried by the response events.
   * @return the unregistered subscriber to the response events.
   */
  fun <V> poll(attachment: T): Subscriber<V>

  /**
   * Appends a header to the event.
   *
   * @param key   the header key.
   * @param value the header value associated to the key. If the key is already registered,
   *              the value will be appended to the existing values.
   * @return this event emitter
   */
  fun withHeader(key: String, value: String): Emitter<T>

  /**
   * Appends a value to this event's header.
   *
   * @param key the header key.
   * @param values the header values associated to the key. If the key is already registered,
   *               the values will be appended to the existing ones.
   * @return this event emitter
   */
  fun withHeader(key: String, vararg values: String): Emitter<T>

  /**
   * Appends several values to this event's header.
   *
   * @param values the header values to be appended. If a key is already registered,
   *               the values will be appended to the existing ones.
   * @return this event emitter
   */
  fun withHeader(values: Multimap<String, Any>): Emitter<T>

  /**
   * Registers a dead letter sink. Any failed attempts to deliver this event will be
   * routed to the consumer provided.
   *
   * @param deadLetterSink the consumer of dead letters.
   * @return this event emitter
   */
  fun withDeadLetterSink(deadLetterSink: Consumer<in Message<T>>): Emitter<T>

  override fun filter(predicate: Predicate<in Message<T>>): Emitter<T>

  override fun intercept(consumer: Consumer<in Message<T>>): Emitter<T>

}
