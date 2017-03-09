package io.duna.core.eventbus.event

import io.duna.core.Context
import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Headers
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.impl.MessageImpl
import org.eclipse.collections.api.multimap.Multimap
import org.eclipse.collections.impl.factory.Multimaps
import java.util.*
import java.util.function.Consumer

internal open class DefaultEvent<T>(private val eventBus: EventBus,
                                    override val name: String) : Event<T> {

  private val headers: Headers = Multimaps.mutable.set.empty()

  protected var deadLetterSink: Consumer<in Message<T>>? = null
    private set

  override fun publish(attachment: T?): Event<T> {
    dispatch(attachment)
    return this
  }

  override fun <V> request(attachment: T?): Subscriber<V> {
    return ResponseAwareSubscriber(eventBus, "uuid:${UUID.randomUUID()}") {
      dispatch(attachment, this@ResponseAwareSubscriber.event)
    }
  }

  override fun <V> poll(): Subscriber<V> {
    return ResponseAwareSubscriber(eventBus, "uuid:${UUID.randomUUID()}") {
      dispatch(null, this@ResponseAwareSubscriber.event)
    }
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
  private fun dispatch(attachment: Any?,
                       responseEvent: String? = null) {
    val message = if (attachment is Throwable)
      MessageImpl(
        source = (Context.Companion.currentContext()?.get("duna:current-subscriber") as Subscriber<*>?)?.event,
        target = name,
        responseEvent = responseEvent,
        attachment = attachment,
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
      .dispatch(message)
      .onError { deadLetterSink?.accept(message as Message<T>) }
  }
}
