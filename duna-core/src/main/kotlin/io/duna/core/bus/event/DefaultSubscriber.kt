package io.duna.core.bus.event

import io.duna.core.bus.OldEventBus
import io.duna.core.bus.Message
import io.duna.core.concurrent.execution.ExecutionContext
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import java.nio.BufferOverflowException
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Consumer
import kotlin.concurrent.write
import io.reactivex.Emitter as RxEmitter

internal open class DefaultSubscriber<T>(override val event: String,
                                         private  val eventBus: OldEventBus,
                                         private  val executionContext: ExecutionContext,
                                         private  val maxCacheSize: Int = 10) : Subscriber<T> {

  override final val isBlocking: Boolean
    get() = executionContext.blocking

  private var messageSink: Consumer<in Message<T>>? = null
  private var errorSink: Consumer<in Message<T>>? = null

  private val messageQueue = LinkedList<Message<T>>()

  private var emitter: FlowableEmitter<T>? = null
  private val flowable: AtomicReference<Flowable<T>?> = AtomicReference(null)

  override fun onNext(messageSink: Consumer<in Message<T>>): Subscriber<T> {
    this.messageSink = messageSink

    executionContext.execute {
      val iterator = messageQueue.iterator()

      while (iterator.hasNext()) {
        val next = iterator.next()
        if (!next.isError) {
          messageSink.accept(next)
          iterator.remove()
        } else if (errorSink != null) {
          errorSink?.accept(messageQueue.poll())
          iterator.remove()
        }
      }
    }

    return this
  }

  override fun onError(errorSink: Consumer<in Message<T>>): Subscriber<T> {
    this.errorSink = errorSink

    executionContext.execute {
      val iterator = messageQueue.iterator()

      while (iterator.hasNext()) {
        val next = iterator.next()
        if (next.isError) {
          errorSink.accept(next)
          iterator.remove()
        } else if (messageSink != null) {
          messageSink?.accept(messageQueue.poll())
          iterator.remove()
        }
      }
    }

    return this
  }

  override fun toFlowable(backpressureStrategy: BackpressureStrategy): Flowable<T> {
    if (flowable.get() != null)
      return flowable.get()!!

    var newEmitter: FlowableEmitter<T>? = null
    val newFlowable = Flowable
      .create<T>({ newEmitter = it }, BackpressureStrategy.BUFFER)
      .share()

    if (flowable.compareAndSet(null, newFlowable)) {
      emitter = newEmitter
    }

    return flowable.get()!!
  }

  override fun cancel(): Boolean = eventBus.expunge(this)

  override fun blocking(): Subscriber<T> {
    executionContext.blocking = true
    return this
  }

  open internal fun process(message: Message<T>) = executionContext.execute {
    if (!message.isError) doProcess(message, messageSink)
    else doProcess(message, errorSink)
  }

  private fun doProcess(message: Message<T>, callback: Consumer<in Message<T>>?) {
    if (flowable.get() == null && callback == null) {
      if (messageQueue.size == maxCacheSize)
        throw BufferOverflowException()

      messageQueue.offer(message)
    } else {
      emitter?.onNext(message.attachment)
      callback?.accept(message)
    }
  }
}
