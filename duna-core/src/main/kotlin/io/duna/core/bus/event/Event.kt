package io.duna.core.bus.event

import io.duna.core.bus.EventBus
import io.duna.core.bus.Message
import org.eclipse.collections.api.multimap.Multimap
import java.util.function.Consumer

/**
 * Represents an event emit.
 *
 * _Thread-safety:_ unless otherwise explicitly noted, implementations should not be considered thread-safe.
 *
 * @param T the type of attachment the event messages must carry.
 *
 * @author [Carlos Eduardo Melo][hk@cemelo.com]
 * @see [EventBus]
 */
interface Event<T> {

  /**
   * The event name.
   */
  val name: String

  /**
   * Emits this event to all subscribers.
   *
   * @param attachment the attachment carried by this event.
   * @return this event.
   */
  fun dispatch(attachment: T?): Event<T>

  /**
   * Emits this event to all subscribers.
   *
   * @return this event.
   */
  fun dispatch(): Event<T> = dispatch(null)

  /**
   * Emits this event and creates a [Subscriber] builder to receive the responses.
   *
   * The request will only be dispatched by the [EventBus] after the [Subscriber]
   * is registered through [Subscriber.register].
   *
   * @param attachment  the attachment carried by this event.
   * @param V           the type of the attachment carried by the response event.
   * @return a builder to create a subscriber listening for responses.
   */
  fun <V> request(attachment: T?, onReplyReceived: (Message<V>) -> Unit): Subscriber<V>

  /**
   * Emits this event and creates a [Subscriber] builder to receive the responses.
   *
   * The request will only be dispatched by the [EventBus] after the [Subscriber]
   * is registered through [Subscriber.register].
   *
   * @param attachment  the attachment carried by this event.
   * @param V           the type of the attachment carried by the response event.
   * @return a builder to create a subscriber listening for responses.
   */
  fun <V> request(onReplyReceived: (Message<V>) -> Unit): Subscriber<V> = request(null, onReplyReceived)

  /**
   * FIXME document
   */
  fun <V> poll(): Subscriber<V>

  /**
   * Registers a dead letter sink. Any failed attempts to deliver this event will be
   * routed to the consumer provided.
   *
   * @param deadLetterSink the consumer of dead letters.
   * @return this event.
   */
  fun onDeadLetter(deadLetterSink: Consumer<in Message<T>>): Event<T>

  /**
   * Registers a dead letter sink. Any failed attempts to deliver this event will be
   * routed to the consumer provided.
   *
   * @param deadLetterSink the consumer of dead letters.
   * @return this event emit.
   */
  fun onError(deadLetterSink: (Message<T>) -> Unit) =
    onDeadLetter(Consumer(deadLetterSink::invoke))

  /**
   * Appends a header to the event.
   *
   * @param key   the header key.
   * @param value the header value associated to the key. If the key is already registered,
   *              the value will be appended to the existing values.
   * @return this event emit.
   */
  fun putHeader(key: String, value: String): Event<T>

  /**
   * Appends a value to this event's header.
   *
   * @param key the header key.
   * @param values the header values associated to the key. If the key is already registered,
   *               the values will be appended to the existing ones.
   * @return this event emit.
   */
  fun putHeader(key: String, vararg values: String): Event<T>

  /**
   * Appends several values to this event's header.
   *
   * @param values the header values to be appended. If a key is already registered,
   *               the values will be appended to the existing ones.
   * @return this event emit.
   */
  fun putHeader(values: Multimap<String, String>): Event<T>
}
