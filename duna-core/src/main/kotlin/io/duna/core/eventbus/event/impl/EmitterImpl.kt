package io.duna.core.eventbus.event.impl

import io.duna.core.Context
import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Headers
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.Emitter
import io.duna.core.eventbus.event.Event
import io.duna.core.eventbus.event.Subscriber
import io.duna.core.eventbus.impl.MessageImpl
import io.duna.core.eventbus.impl.MessageType
import io.duna.core.eventbus.routing.clauses.routeTo
import org.eclipse.collections.api.multimap.Multimap
import org.eclipse.collections.impl.factory.Multimaps
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

internal class EmitterImpl<T>(private val eventBus: EventBus,
                              override val name: String) : Emitter<T> {

  private val headers: Headers = Multimaps.mutable.set.empty()
  private val filters: MutableList<Predicate<in Message<T>>> = LinkedList()
  private val interceptors: MutableList<Consumer<in Message<T>>> = LinkedList()

  private var deadLetterSink: Consumer<in Message<T>>? = null

  override fun emit(attachment: T?): Emitter<T> {
    val message = MessageImpl(
      source = (Context.currentContext()?.get("duna:current-event") as Event<*>).name,
      target = name,
      attachment = attachment,
      headers = headers
    )

    eventBus
      .router()
      .route(message)
      .onError { deadLetterSink?.accept(message) }

    return this
  }

  override fun publish(attachment: T?): Emitter<T> {
    val message = MessageImpl(
      source = (Context.currentContext()?.get("duna:current-event") as Event<*>).name,
      target = name,
      attachment = attachment,
      headers = headers,
      messageType = MessageType.MULTICAST
    )

    eventBus
      .router()
      .route(message)
      .onError { deadLetterSink?.accept(message) }

    return this
  }

  override fun <V> request(attachment: T?): Subscriber<V> {
    val subscriber = CachedSingleResponseSubscriber<V>(eventBus, "uuid:${UUID.randomUUID()}")

    val message = MessageImpl(
      source = (Context.currentContext()?.get("duna:current-event") as Event<*>).name,
      target = name,
      responseEvent = subscriber.name,
      attachment = attachment,
      headers = headers
    )

    // FIXME Should register the subscriber before routing the message
    eventBus.router().to(subscriber.name, routeTo(subscriber))

    eventBus
      .router()
      .route(message)
      .onError { deadLetterSink?.accept(message) }

    return subscriber
  }

  override fun <V> poll(attachment: T?): Subscriber<V> {
    val subscriber = CachedMultipleResponseSubscriber<V>(eventBus, "uuid:${UUID.randomUUID()}")

    val message = MessageImpl(
      source = (Context.currentContext()?.get("duna:current-event") as Event<*>).name,
      target = name,
      responseEvent = subscriber.name,
      attachment = attachment,
      headers = headers,
      messageType = MessageType.MULTICAST
    )

    eventBus
      .router()
      .route(message)
      .onError { deadLetterSink?.accept(message) }

    return subscriber
  }

  override fun withHeader(key: String, value: String): Emitter<T> {
    headers[key] += value
    return this
  }

  override fun withHeader(key: String, vararg values: String): Emitter<T> {
    headers[key] += values
    return this
  }

  override fun withHeader(values: Multimap<String, String>): Emitter<T> {
    headers.putAll(values)
    return this
  }

  override fun withDeadLetterSink(deadLetterSink: Consumer<in Message<T>>): Emitter<T> {
    this.deadLetterSink = deadLetterSink
    return this
  }

  override fun filter(predicate: Predicate<in Message<T>>): Emitter<T> {
    filters += predicate
    return this
  }

  override fun intercept(interceptor: Consumer<in Message<T>>): Emitter<T> {
    interceptors += interceptor
    return this
  }
}
