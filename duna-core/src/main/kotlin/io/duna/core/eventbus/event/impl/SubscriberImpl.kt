package io.duna.core.eventbus.event.impl

import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.Subscriber
import io.reactivex.Flowable
import java.util.function.Consumer
import java.util.function.Predicate

internal class SubscriberImpl<T>(private val event: String) : Subscriber<T> {

  override fun register() {
    TODO("not implemented")
  }

  override fun purge() {
    TODO("not implemented")
  }

  override fun consume(consumer: Consumer<in Message<T>>) {
    TODO("not implemented")
  }

  override fun getName(): String {
    TODO("not implemented")
  }

  override fun toFlowable(): Flowable<T> {
    TODO("not implemented")
  }

  override fun blocking(): Subscriber<T> {
    TODO("not implemented")
  }

  override fun withErrorSink(errorSink: Consumer<in Message<Throwable>>): Subscriber<T> {
    TODO("not implemented")
  }

  override fun filter(predicate: Predicate<in Message<T>>): Subscriber<T> {
    TODO("not implemented")
  }

  override fun intercept(consumer: Consumer<in Message<T>>): Subscriber<T> {
    TODO("not implemented")
  }
}
