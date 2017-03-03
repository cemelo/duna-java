package io.duna.core.eventbus.routing.clauses

import io.duna.core.eventbus.event.Subscriber

fun routeTo(subscriber: Subscriber<*>): RouteDefinition {
  val init: RouteDefinition.() -> Unit = {
    choose { subscriber.accept(it) }
  }

  val route = RouteDefinition()
  route.init()
  return route
}
