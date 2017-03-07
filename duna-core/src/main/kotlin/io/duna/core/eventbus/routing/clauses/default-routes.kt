package io.duna.core.eventbus.routing.clauses

import io.duna.core.eventbus.Message
import io.duna.core.eventbus.event.Subscriber

fun staticRoute(subscriber: Subscriber<*>): RouteDefinition.(Message<*>) -> Unit {
  return { deliverTo(subscriber.name) }
}
