package io.duna.core.eventbus.event.impl

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.Emitter
import io.duna.core.eventbus.event.Subscriber
import org.eclipse.collections.api.multimap.Multimap
import java.util.function.Consumer
import java.util.function.Predicate

internal class EmitterImpl<T>(private val event: String) : Emitter<T> {
  override fun register() {
    TODO("not implemented")
  }

  override fun purge() {
    TODO("not implemented")
  }

  override fun emit(attachment: T): Future<Emitter<T>> {
    TODO("not implemented")
  }

  override fun getName(): String {
    TODO("not implemented")
  }

  override fun publish(attachment: T): Future<Emitter<T>> {
    TODO("not implemented")
  }

  override fun <V> request(attachment: T): Subscriber<V> {
    TODO("not implemented")
  }

  override fun <V> poll(attachment: T): Subscriber<V> {
    TODO("not implemented")
  }

  override fun withHeader(key: String, value: String): Emitter<T> {
    TODO("not implemented")
  }

  override fun withHeader(key: String, vararg values: String): Emitter<T> {
    TODO("not implemented")
  }

  override fun withHeader(values: Multimap<String, Any>): Emitter<T> {
    TODO("not implemented")
  }

  override fun withDeadLetterSink(deadLetterSink: Consumer<in Message<T>>): Emitter<T> {
    TODO("not implemented")
  }

  override fun filter(predicate: Predicate<in Message<T>>): Emitter<T> {
    TODO("not implemented")
  }

  override fun intercept(consumer: Consumer<in Message<T>>): Emitter<T> {
    TODO("not implemented")
  }
}
