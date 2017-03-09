package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Consumer
import kotlin.concurrent.read
import kotlin.concurrent.write
import io.reactivex.Emitter as RxEmitter

internal open class DefaultSubscriber<T>(private val eventBus: EventBus,
                                         override val event: String) : Subscriber<T> {

  override final var blocking: Boolean = false
    private set

  private val successfulMessagesBuffer: Queue<Message<T>> = ConcurrentLinkedQueue()
  private val failedMessagesBuffer: Queue<Message<T>> = ConcurrentLinkedQueue()

  private val activeFlowableEmitters: MutableSet<FlowableEmitter<T>> = Collections.newSetFromMap(ConcurrentHashMap())

  private var messageSink: Consumer<in Message<T>>? = null
  private var errorSink: Consumer<in Message<T>>? = null

  private val sinkLock = ReentrantReadWriteLock(true)

  override fun onNext(messageSink: Consumer<in Message<T>>): Subscriber<T> {
    sinkLock.write {
      this.messageSink = messageSink

      while (successfulMessagesBuffer.isNotEmpty())
        messageSink.accept(successfulMessagesBuffer.poll())
    }

    return this
  }

  override fun onError(errorSink: Consumer<in Message<T>>): Subscriber<T> {
    sinkLock.write {
      this.errorSink = errorSink

      while (failedMessagesBuffer.isNotEmpty())
        errorSink.accept(failedMessagesBuffer.poll())
    }

    return this
  }

  override fun toFlowable(backpressureStrategy: BackpressureStrategy): Flowable<T> {
    return Flowable.create<T>({ emitter ->
      activeFlowableEmitters.add(emitter)
      emitter.setCancellable { activeFlowableEmitters.remove(emitter) }

      if (emitter.isCancelled) {
        activeFlowableEmitters.remove(emitter)
      } else {
        while (successfulMessagesBuffer.isNotEmpty())
          emitter.onNext(successfulMessagesBuffer.poll().attachment)

        while (failedMessagesBuffer.isNotEmpty())
          emitter.onError(failedMessagesBuffer.poll().cause)
      }
    }, backpressureStrategy).share()
  }

  override fun register(): Boolean {
    TODO("not implemented")
  }

  override fun cancel(): Boolean {
    TODO("not implemented")
  }

  override fun blocking(): Subscriber<T> {
    this.blocking = true
    return this
  }

  internal fun process(message: Message<T>) {
    activeFlowableEmitters.forEach {
      if (message.succeeded()) it.onNext(message.attachment)
      else it.onError(message.cause)
    }

    if (message.succeeded()) sinkLock.read {
      messageSink?.accept(message) ?:
        if (activeFlowableEmitters.isEmpty())
          successfulMessagesBuffer.add(message)
    } else sinkLock.read {
      errorSink?.accept(message) ?:
        if (activeFlowableEmitters.isEmpty())
          failedMessagesBuffer.add(message)
    }
  }

}
