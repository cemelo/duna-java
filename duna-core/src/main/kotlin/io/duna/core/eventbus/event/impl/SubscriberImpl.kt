package io.duna.core.eventbus.event.impl

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.Subscriber
import io.reactivex.Flowable
import java.util.function.Consumer
import java.util.function.Predicate

internal open class SubscriberImpl<T>(private val eventBus: EventBus,
                                      override val name: String) : Subscriber<T> {
  private var blocking = false

  private var consumer: Consumer<in Message<T>>? = null

  private var errorSink: Consumer<in Message<Throwable>>? = null
  private var filter: Predicate<in Message<T>>? = null

  private var interceptor: Consumer<in Message<T>>? = null
  override fun consume(consumer: Consumer<in Message<T>>) {
    TODO("not implemented")
  }

  override fun toFlowable(): Flowable<T> {
    TODO("not implemented")
  }

  override fun blocking(): Subscriber<T> {
    this.blocking = true
    return this
  }

  override fun accept(message: Message<*>) {
    TODO("not implemented")
  }

  override fun withErrorSink(errorSink: Consumer<in Message<Throwable>>): Subscriber<T> {
    this.errorSink = errorSink
    return this
  }

  override fun filter(predicate: Predicate<in Message<T>>): Subscriber<T> {
    this.filter = predicate
    return this
  }

  override fun intercept(interceptor: Consumer<in Message<T>>): Subscriber<T> {
    this.interceptor = interceptor
    return this
  }
}
