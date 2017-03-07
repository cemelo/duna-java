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

  val source: String?

  val target: String

  val responseEvent: String?

  val headers: Headers

  val attachment: T?

  val cause: Throwable?

  fun header(key: String): Set<String> = headers[key].toHashSet()

  fun succeeded() = cause == null

  fun failed() = !succeeded()

}
