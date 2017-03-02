package io.duna.core.eventbus.impl

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.Emitter
import io.duna.core.eventbus.event.Subscriber
import io.duna.core.eventbus.queue.MessageQueue
import io.duna.core.eventbus.routing.Router
import java.util.function.Consumer
import java.util.function.Predicate

internal class LocalEventBus : EventBus {

  private val pipeline = PipelineImpl()

  override fun <T> emitter(event: String): Emitter<T> {
    TODO("not implemented")
  }

  override fun <T> subscriber(event: String): Subscriber<T> {
    TODO("not implemented")
  }

  override fun <T> queue(name: String): MessageQueue<T> {
    TODO("not implemented")
  }

  override fun pipeline() = pipeline

  override fun router(): Router {
    TODO("not implemented")
  }

  override fun filter(filter: Predicate<Message<*>>): EventBus {
    TODO("not implemented")
  }

  override fun intercept(interceptor: Consumer<Message<*>>): EventBus {
    TODO("not implemented")
  }
}
