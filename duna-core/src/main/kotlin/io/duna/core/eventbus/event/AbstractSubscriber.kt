package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.routing.clauses.staticRoute
import java.util.function.Consumer
import java.util.function.Predicate

abstract class AbstractSubscriber<T>(protected val eventBus: EventBus,
                                     override val name: String) : Subscriber<T> {

  override var blocking: Boolean = false
    protected set

  protected var consumer: Consumer<in Message<T>>? = null

  protected var errorSink: Consumer<in Message<T>>? = null
    private set

  protected var filter: Predicate<in Message<T>>? = null
    private set

  protected var interceptor: Consumer<in Message<T>>? = null
    private set

  override fun onError(errorSink: Consumer<in Message<T>>): Subscriber<T> {
    this.errorSink = errorSink
    return this
  }

  override fun register(): Subscriber<T> {
    eventBus
      .router()
      .to(this.name, staticRoute(this@AbstractSubscriber))

    return this
  }

  override fun blocking(): Subscriber<T> {
    this.blocking = true
    return this
  }

  override fun filter(predicate: Predicate<in Message<T>>): Subscriber<T> {
    this.filter = predicate
    return this
  }

  override fun intercept(interceptor: Consumer<in Message<T>>): EventProcessor<T> {
    this.interceptor = interceptor
    return this
  }
}
