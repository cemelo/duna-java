package io.duna.core.bus.event

import io.duna.core.Context
import io.duna.core.bus.OldEventBus
import io.duna.core.bus.Headers
import io.duna.core.bus.Message
import io.duna.core.bus.impl.MessageImpl
import org.eclipse.collections.api.multimap.Multimap
import org.eclipse.collections.impl.factory.Multimaps
import java.util.*
import java.util.function.Consumer

internal open class DefaultEvent<T>(private val eventBus: OldEventBus,
                                    override val name: String) : Event<T> {

  private val headers: Headers = Multimaps.mutable.set.empty()

  protected var deadLetterSink: Consumer<in Message<T>>? = null
    private set

  override fun dispatch(attachment: T?): Event<T> {
    dispatch(attachment)
    return this
  }

  override fun <V> request(attachment: T?, onReplyReceived: (Message<V>) -> Unit): Subscriber<V> {
    val subscriber = eventBus.subscribe<V>("uuid:${UUID.randomUUID()}")
      .onNext(onReplyReceived)
      .onError(onReplyReceived)

    send(attachment, subscriber.event)

    return subscriber
  }

  override fun <V> poll(): Subscriber<V> {
    val subscriber = eventBus.subscribe<V>("uuid:${UUID.randomUUID()}")
    send(null, subscriber.event)

    return subscriber
  }

  override fun onDeadLetter(deadLetterSink: Consumer<in Message<T>>): Event<T> {
    this.deadLetterSink = deadLetterSink
    return this
  }

  override fun putHeader(key: String, value: String): Event<T> {
    headers[key] += value
    return this
  }

  override fun putHeader(key: String, vararg values: String): Event<T> {
    headers[key] += values
    return this
  }

  override fun putHeader(values: Multimap<String, String>): Event<T> {
    headers.putAll(values)
    return this
  }

  @Suppress("UNCHECKED_CAST")
  private fun send(attachment: Any?,
                   responseEvent: String? = null) {
    val message = if (attachment is Throwable)
      MessageImpl(
        source = (Context.Companion.currentContext()?.get("duna:current-subscriber") as Subscriber<*>?)?.event,
        target = name,
        responseEvent = responseEvent,
        attachment = null,
        cause = attachment,
        headers = headers
      )
    else
      MessageImpl(
        source = (Context.Companion.currentContext()?.get("duna:current-subscriber") as Subscriber<*>?)?.event,
        target = name,
        responseEvent = responseEvent,
        attachment = attachment as T,
        headers = headers
      )

    eventBus
      .dispatch(message as Message<Any>)
      .onError { deadLetterSink?.accept(message as Message<T>) }
  }
}
