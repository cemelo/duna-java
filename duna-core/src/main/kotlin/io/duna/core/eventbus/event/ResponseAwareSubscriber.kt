package io.duna.core.eventbus.event

import io.duna.core.eventbus.EventBus

internal class ResponseAwareSubscriber<T>(eventBus: EventBus,
                                          event: String,
                                          private val onRegister: ResponseAwareSubscriber<T>.() -> Unit)
  : DefaultSubscriber<T>(eventBus, event) {
}
