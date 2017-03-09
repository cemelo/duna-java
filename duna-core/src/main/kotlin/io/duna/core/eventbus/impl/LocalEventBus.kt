package io.duna.core.eventbus.impl

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.DefaultEvent
import io.duna.core.eventbus.event.DefaultSubscriber
import io.duna.core.eventbus.event.Event
import io.duna.core.eventbus.event.Subscriber
import io.duna.core.eventbus.queue.MessageQueue
import io.duna.core.eventbus.routing.LocalRouter
import io.duna.core.eventbus.routing.Router
import io.netty.channel.EventLoop
import io.netty.channel.EventLoopGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService

internal class LocalEventBus(private val eventLoopGroup: EventLoopGroup,
                             private val workerPool: ExecutorService) : EventBus {

  private val pipeline = PipelineImpl()
  private val router = LocalRouter()
  private val messageQueues = ConcurrentHashMap<String, MessageQueue<*>>()

  private val eventExecutorMap = ConcurrentHashMap<Subscriber<*>, EventLoop>()

  override fun <T> emit(name: String): Event<T> = DefaultEvent(this, name)

  override fun <T> subscriber(event: String): Subscriber<T> {
    val subscriber = DefaultSubscriber<T>(this, event)
    eventExecutorMap[subscriber] = eventLoopGroup.next()

    return subscriber
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> queue(name: String): MessageQueue<T> = messageQueues.computeIfAbsent(name) {
    TODO()
  } as MessageQueue<T>

  override fun router(): Router = router

  override fun accept(message: Message<*>) {

  }

  override fun dispatch(message: Message<*>): Future<Message<*>> {
    accept(message)
    return Future.completedFuture()
  }

  override fun purge(subscriber: Subscriber<*>): Boolean {
    TODO("not implemented")
  }
}
