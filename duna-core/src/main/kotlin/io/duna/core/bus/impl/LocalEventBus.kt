package io.duna.core.bus.impl

import io.duna.core.bus.EventBus
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
                             private val workerPool: ExecutorService) : EventBus {

  private val router = LocalRouter()
  private val messageQueues = ConcurrentHashMap<String, MessageQueue<Any>>()

  private val subscribers = ConcurrentHashMap<String, Subscriber<Any>>()
  private val eventExecutorMap = ConcurrentHashMap<Subscriber<Any>, EventLoop>()

  override fun <T> emit(name: String): Event<T> = DefaultEvent(this, name)

  override fun <T> subscribe(toEvent: String): Subscriber<T> { TODO() } // = DefaultSubscriber(toEvent, this)

  @Suppress("UNCHECKED_CAST")
  override fun <T> queue(name: String): MessageQueue<T> = messageQueues.computeIfAbsent(name) {
    TODO()
  } as MessageQueue<T>

  override fun accept(message: Message<Any>) {
    if (!subscribers.containsKey(message.target)) {
      throw UnknownSubscriberException()
    }

    val subscriber = subscribers[message.target] as DefaultSubscriber<Any>
    val block = Runnable {
      // FIXME should setup context before processing the message
      subscriber.process(message)
    }

    if (subscriber.isBlocking) {
      workerPool.submit(block)
    } else {
      val executor = eventExecutorMap[subscriber]
      // executor?.submit(block) ?: throw RuntimeException("No eventloop assigned to subscriber.")
    }
  }

  override fun dispatch(message: Message<Any>): Future<Message<Any>> {
    accept(message)
    return Future.completedFuture()
  }

  override fun expunge(subscriber: Subscriber<*>): Boolean {
    subscribers.remove(subscriber.event)

    if (!subscriber.isBlocking)
      eventExecutorMap.remove(subscriber)

    return true
  }

  private fun register(subscriber: Subscriber<Any>): Boolean {
//    if (!subscriber.isBlocking)
//      eventExecutorMap[subscriber] = eventLoopGroup.next()

    subscribers[subscriber.event] = subscriber

    return true
  }
}
