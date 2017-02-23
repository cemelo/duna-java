package io.duna.core.eventbus

import io.duna.core.eventbus.event.Emitter
import io.duna.core.eventbus.event.Event
import io.duna.core.eventbus.event.Subscriber
import io.duna.core.eventbus.queue.MessageQueue
import io.duna.core.eventbus.routing.Router
import java.util.function.BiConsumer
import java.util.function.BiPredicate

interface EventBus {

  fun <T> emitter(event: String): Emitter<T>

  fun <T> subscriber(event: String): Subscriber<T>

  fun <T> queue(name: String): MessageQueue<T>

  fun pipeline(): Pipeline

  fun router(): Router

  fun addEventFilter(filter: BiPredicate<Event<*>, Message<*>>)

  fun addEventInterceptor(interceptor: BiConsumer<Event<*>, Message<*>>)

}

