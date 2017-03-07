package io.duna.core.eventbus.event

import io.duna.core.Context
import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Headers
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.impl.MessageImpl
import io.duna.core.eventbus.impl.MessageType
import org.eclipse.collections.api.multimap.Multimap
import org.eclipse.collections.impl.factory.Multimaps
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

internal open class DefaultEmitter<T>(private val eventBus: EventBus,
                                      override val name: String) : Emitter<T> {

  private val headers: Headers = Multimaps.mutable.set.empty()

  protected var filter: Predicate<in Message<T>>? = null
    private set

  protected var interceptor: Consumer<in Message<T>>? = null
    private set

  protected var deadLetterSink: Consumer<in Message<T>>? = null
    private set

  override fun emit(attachment: T?): Emitter<T> {
    dispatch(attachment, messageType = MessageType.UNICAST)
    return this
  }

  override fun publish(attachment: T?): Emitter<T> {
    dispatch(attachment, messageType = MessageType.MULTICAST)
    return this
  }

  override fun <V> request(attachment: T?): Subscriber<V> {
    return ResponseSubscriber(eventBus, "uuid:${UUID.randomUUID()}") {
      dispatch(attachment, this@ResponseSubscriber.name, MessageType.MULTICAST)
    }
  }

  override fun <V> poll(attachment: T?): Subscriber<V> {
    return ResponseSubscriber(eventBus, "uuid:${UUID.randomUUID()}") {
      dispatch(attachment, this@ResponseSubscriber.name, MessageType.MULTICAST)
    }
  }

  override fun onDeadLetter(deadLetterSink: Consumer<in Message<T>>): Emitter<T> {
    this.deadLetterSink = deadLetterSink
    return this
  }

  override fun putHeader(key: String, value: String): Emitter<T> {
    headers[key] += value
    return this
  }

  override fun putHeader(key: String, vararg values: String): Emitter<T> {
    headers[key] += values
    return this
  }

  override fun putHeader(values: Multimap<String, String>): Emitter<T> {
    headers.putAll(values)
    return this
  }

  override fun filter(predicate: Predicate<in Message<T>>): Emitter<T> {
    this.filter = predicate
    return this
  }

  override fun intercept(interceptor: Consumer<in Message<T>>): EventProcessor<T> {
    this.interceptor = interceptor
    return this
  }

  @Suppress("UNCHECKED_CAST")
  private fun dispatch(attachment: Any?,
                       responseEvent: String? = null,
                       messageType: MessageType = MessageType.UNICAST) {
    val message = if (attachment is Throwable)
      MessageImpl(
        source = (Context.Companion.currentContext()?.get("duna:current-event") as EventProcessor<*>?)?.name,
        target = name,
        responseEvent = responseEvent,
        attachment = attachment,
        headers = headers,
        messageType = messageType
      )
    else
      MessageImpl(
        source = (Context.Companion.currentContext()?.get("duna:current-event") as EventProcessor<*>?)?.name,
        target = name,
        responseEvent = responseEvent,
        attachment = attachment as T,
        headers = headers,
        messageType = messageType
      )

    interceptor?.accept(message as Message<T>)
    if (!(filter?.test(message as Message<T>) ?: true)) return

    eventBus
      .dispatch(message)
      .onError { deadLetterSink?.accept(message as Message<T>) }
  }
}
