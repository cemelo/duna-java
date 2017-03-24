package io.duna.core.bus.routing.clauses

import io.duna.core.bus.Message
import io.duna.core.bus.event.Subscriber

fun staticRoute(subscriber: Subscriber<*>): RouteDefinition.(Message<*>) -> Unit {
  return { deliverTo(subscriber.event) }
}
