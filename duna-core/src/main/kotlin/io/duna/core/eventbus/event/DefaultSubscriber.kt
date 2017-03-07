package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import io.reactivex.Emitter as RxEmitter

internal open class DefaultSubscriber<T>(eventBus: EventBus,
                                         name: String) : AbstractSubscriber<T>(eventBus, name) {

  private var flowable: Flowable<T>? = null
  private val activeFlowableEmitters: MutableSet<FlowableEmitter<T>> = Collections.newSetFromMap(ConcurrentHashMap())

  override fun onNext(consumer: Consumer<in Message<T>>): Subscriber<T> {
    this.consumer = consumer
    return this
  }

  override fun toFlowable(): Flowable<T> {
    return Flowable.create<T>({ emitter ->
      activeFlowableEmitters += emitter
      emitter.setCancellable { activeFlowableEmitters.remove(emitter) }
    }, BackpressureStrategy.BUFFER)
      .share()
  }

  @Suppress("UNCHECKED_CAST")
  override fun accept(message: Message<*>) {
    interceptor?.accept(message as Message<T>)
    if (!(filter?.test(message as Message<T>) ?: true)) return

    activeFlowableEmitters.forEach {
      if (message.failed()) it.onError(message.cause)
      else if (message.attachment != null) it.onNext(message.attachment as T)
    }

    if (message.failed()) errorSink?.accept(message as Message<T>)
    else consumer?.accept(message as Message<T>)
  }

}
