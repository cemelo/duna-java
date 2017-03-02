package io.duna.core.eventbus

/**
 * Represents a message carried by the event bus.
 *
 * Messages are representations of events emitted by the application. They
 * are responsible for providing a context to enable the event bus routing.
 * They also carry an attachment which will be consumed by the subscribers.
 *
 * The default implementation is not thread safe.
 *
 * @author [Carlos Eduardo Melo][ceduardo.melo@gmail.com]
 */
interface Message<out T> {

  fun getSource(): String

  fun getTarget(): String

  fun getResponseEvent(): String

  fun getHeaders(): Headers

  fun getAttachment(): T

  fun header(key: String): Set<String> = getHeaders()[key].toHashSet()
}
