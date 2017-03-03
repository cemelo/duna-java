package io.duna.core.eventbus.event.impl

import io.duna.core.eventbus.EventBus

internal class CachedMultipleResponseSubscriber<T>(private val eventBus: EventBus,
                                                   private val event: String) : SubscriberImpl<T>(eventBus, event) {
}
