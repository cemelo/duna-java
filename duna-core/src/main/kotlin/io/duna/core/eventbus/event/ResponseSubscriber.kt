package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus

internal class ResponseSubscriber<T>(eventBus: EventBus,
                                     event: String,
                                     private val onRegister: ResponseSubscriber<T>.() -> Unit)
  : DefaultSubscriber<T>(eventBus, event) {

  override fun register(): Subscriber<T> {
    super.register()
    this.onRegister()

    return this
  }
}
