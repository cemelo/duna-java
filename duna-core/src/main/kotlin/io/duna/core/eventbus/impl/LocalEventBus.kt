package io.duna.core.eventbus.impl

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.*
import io.duna.core.eventbus.queue.MessageQueue
import io.duna.core.eventbus.routing.LocalRouter
import io.duna.core.eventbus.routing.Router
import io.netty.channel.EventLoop
import io.netty.channel.EventLoopGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.function.Consumer
import java.util.function.Predicate

internal class LocalEventBus(private val eventLoopGroup: EventLoopGroup,
                             private val workerPool: ExecutorService) : EventBus {

  private val pipeline = PipelineImpl()
  private val router = LocalRouter()
  private val messageQueues = ConcurrentHashMap<String, MessageQueue<*>>()

  private val eventExecutorMap = ConcurrentHashMap<EventProcessor<*>, EventLoop>()

  override var filter: Predicate<Message<*>>? = null
  override var interceptor: Consumer<Message<*>>? = null

  override fun <T> emitter(event: String): Emitter<T> = DefaultEmitter(this, event)

  override fun <T> subscriber(event: String): Subscriber<T> {
    val subscriber = DefaultSubscriber<T>(this, event)
    eventExecutorMap[subscriber] = eventLoopGroup.next()

    return subscriber
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> queue(name: String): MessageQueue<T> = messageQueues.computeIfAbsent(name) {
    TODO()
  } as MessageQueue<T>

  override fun pipeline() = pipeline

  override fun router(): Router = router

  override fun filter(filter: Predicate<Message<*>>): EventBus {
    TODO("not implemented")
  }

  override fun intercept(interceptor: Consumer<Message<*>>): EventBus {
    TODO("not implemented")
  }

  override fun accept(message: Message<*>) {
    interceptor?.accept(message)
    if (!(filter?.test(message) ?: true)) return

    val transformed = pipeline().transform(message)

  }

  override fun dispatch(message: Message<*>): Future<Message<*>> {
    accept(message)
    return Future.completedFuture()
  }

  override fun purge(subscriber: Subscriber<*>): Boolean {
    TODO("not implemented")
  }
}
