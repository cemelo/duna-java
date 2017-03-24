package io.duna.core.bus.event

import io.duna.core.bus.EventBus
import io.duna.core.bus.Message
import java.util.function.Consumer

abstract class AbstractSubscriber<T>(protected val eventBus: EventBus,
                                     override val event: String) : Subscriber<T> {

  override var isBlocking: Boolean = false
    protected set

  protected var consumer: Consumer<in Message<T>>? = null

  protected var errorSink: Consumer<in Message<T>>? = null
    private set

  override fun onError(errorSink: Consumer<in Message<T>>): Subscriber<T> {
    this.errorSink = errorSink
    return this
  }

  override fun blocking(): Subscriber<T> {
    this.isBlocking = true
    return this
  }
}
