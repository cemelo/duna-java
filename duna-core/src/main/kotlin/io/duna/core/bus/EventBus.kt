package io.duna.core.bus

import io.duna.core.bus.event.Event
import io.duna.core.bus.event.Subscriber

interface EventBus {

  fun <T> emit(event: String, attachment: T? = null, init: Event<T>.() -> Unit): Event<T>

  fun <T> subscribe(event: String, init: Subscriber<T>.() -> Unit): Subscriber<T>

  fun <T> subscribeOnce(event: String, init: Subscriber<T>.() -> Unit): Subscriber<T>

  companion object {
    internal val EMPTY_ERROR_HANDLER: (Throwable) -> Unit = {}
  }
}
