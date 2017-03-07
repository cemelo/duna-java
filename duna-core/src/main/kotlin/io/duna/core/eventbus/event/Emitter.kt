package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import org.eclipse.collections.api.multimap.Multimap
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Represents an event emitter.
 *
 * _Thread-safety:_ unless otherwise explicitly noted, implementations should not be considered thread-safe.
 *
 * @param T the type of attachment the event messages must carry.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 * @see [EventBus]
 */
interface Emitter<T> : EventProcessor<T> {

  /**
   * Emits this event a single node. If there is more than subscriber to this event,
   * the [EventBus] will select one to onNext it according to the ESB configuration.
   *
   * @param attachment the attachment carried by this event.
   * @return this event emitter.
   */
  fun emit(attachment: T?): Emitter<T>

  /**
   * Emits this event a single node. If there is more than subscriber to this event,
   * the [EventBus] will select one to onNext it according to the ESB configuration.
   *
   * @return this event emitter.
   */
  fun emit(): Emitter<T> = emit(null)

  /**
   * Emits this event to all subscribers.
   *
   * @param attachment the attachment carried by this event.
   * @return this event emitter.
   */
  fun publish(attachment: T?): Emitter<T>

  /**
   * Emits this event to all subscribers.
   *
   * @return this event emitter.
   */
  fun publish(): Emitter<T> = publish(null)

  /**
   * Emits this event and initiates a subscriber to receive a response. If there is more than subscriber
   * to this event, the [EventBus] will select one to onNext it according to the ESB configuration.
   *
   * The request will only be dispatched by the [EventBus] after the [Subscriber]
   * is registered through [Subscriber.register].
   *
   * @param attachment  the attachment carried by this event.
   * @param V           the type of the attachment carried by the response event.
   * @return the unregistered subscriber to the response event.
   */
  fun <V> request(attachment: T?): Subscriber<V>

  /**
   * Emits this event and initiates a subscriber to receive a response. If there is more than subscriber
   * to this event, the [EventBus] will select one to onNext it according to the ESB configuration.
   *
   * The request will only be dispatched by the [EventBus] after the [Subscriber]
   * is registered through [Subscriber.register].
   *
   * @param V           the type of the attachment carried by the response event.
   * @return the unregistered subscriber to the response event.
   */
  fun <V> request(): Subscriber<V> = request(null)

  /**
   * Emits this event to all subscribers and initiates a subscriber to receive the responses.
   *
   * The polling request will only be dispatched by the [EventBus] after the [Subscriber]
   * is registered through [Subscriber.register].
   *
   * @param attachment  the attachment carried by this event.
   * @param V           the type of the attachment carried by the response events.
   * @return the unregistered subscriber to the response events.
   */
  fun <V> poll(attachment: T?): Subscriber<V>

  /**
   * Emits this event to all subscribers and initiates a subscriber to receive the responses.
   *
   * The polling request will only be dispatched by the [EventBus] after the [Subscriber]
   * is registered through [Subscriber.register].
   *
   * @param V           the type of the attachment carried by the response events.
   * @return the unregistered subscriber to the response events.
   */
  fun <V> poll(): Subscriber<V> = poll(null)

  /**
   * Registers a dead letter sink. Any failed attempts to deliver this event will be
   * routed to the consumer provided.
   *
   * @param deadLetterSink the consumer of dead letters.
   * @return this event emitter.
   */
  fun onDeadLetter(deadLetterSink: Consumer<in Message<T>>): Emitter<T>

  /**
   * Registers a dead letter sink. Any failed attempts to deliver this event will be
   * routed to the consumer provided.
   *
   * @param deadLetterSink the consumer of dead letters.
   * @return this event emitter.
   */
  fun onDeadLetter(deadLetterSink: (Message<T>) -> Unit) =
    onDeadLetter(Consumer(deadLetterSink::invoke))

  /**
   * Appends a header to the event.
   *
   * @param key   the header key.
   * @param value the header value associated to the key. If the key is already registered,
   *              the value will be appended to the existing values.
   * @return this event emitter.
   */
  fun putHeader(key: String, value: String): Emitter<T>

  /**
   * Appends a value to this event's header.
   *
   * @param key the header key.
   * @param values the header values associated to the key. If the key is already registered,
   *               the values will be appended to the existing ones.
   * @return this event emitter.
   */
  fun putHeader(key: String, vararg values: String): Emitter<T>

  /**
   * Appends several values to this event's header.
   *
   * @param values the header values to be appended. If a key is already registered,
   *               the values will be appended to the existing ones.
   * @return this event emitter.
   */
  fun putHeader(values: Multimap<String, String>): Emitter<T>

  override fun filter(predicate: Predicate<in Message<T>>): Emitter<T>

  override fun filter(predicate: (Message<T>) -> Boolean) = super.filter(predicate) as Emitter<T>

  override fun intercept(interceptor: Consumer<in Message<T>>): EventProcessor<T>

  override fun intercept(interceptor: (Message<T>) -> Unit) = super.intercept(interceptor) as Emitter<T>
}
