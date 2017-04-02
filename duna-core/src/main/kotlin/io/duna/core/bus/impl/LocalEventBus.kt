package io.duna.core.bus.impl

import io.duna.core.bus.OldEventBus
import io.duna.core.bus.Message
import io.duna.core.bus.UnknownSubscriberException
import io.duna.core.bus.event.DefaultEvent
import io.duna.core.bus.event.DefaultSubscriber
import io.duna.core.bus.event.Event
import io.duna.core.bus.event.Subscriber
import io.duna.core.bus.queue.MessageQueue
import io.duna.core.bus.routing.LocalRouter
import io.duna.core.concurrent.Future
import io.duna.core.concurrent.execution.EventLoop
import io.duna.core.concurrent.execution.EventLoopGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService

internal class LocalEventBus(private val eventLoopGroup: EventLoopGroup,
                             private val workerPool: ExecutorService) : OldEventBus {

  private val router = LocalRouter()
  private val messageQueues = ConcurrentHashMap<String, MessageQueue<Any>>()

  private val subscribers = ConcurrentHashMap<String, Subscriber<Any>>()

  override fun <T> emit(name: String): Event<T> = DefaultEvent(this, name)

  override fun <T> subscribe(toEvent: String): Subscriber<T> { TODO() } // = DefaultSubscriber(toEvent, this)

  @Suppress("UNCHECKED_CAST")
  override fun <T> queue(name: String): MessageQueue<T> = messageQueues.computeIfAbsent(name) {
    TODO()
  } as MessageQueue<T>

  override fun accept(message: Message<Any>) {

  }

  override fun dispatch(message: Message<Any>): Future<Message<Any>> {
    accept(message)
    return Future.completedFuture()
  }

  override fun expunge(subscriber: Subscriber<*>): Boolean {
    subscribers.remove(subscriber.event)
    return true
  }

  private fun register(subscriber: Subscriber<Any>): Boolean {
    subscribers[subscriber.event] = subscriber
    return true
  }
}
