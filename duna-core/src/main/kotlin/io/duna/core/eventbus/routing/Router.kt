package io.duna.core.eventbus.routing

import io.duna.core.eventbus.Message
import io.duna.core.eventbus.routing.clauses.RouteDefinition
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap

class Router {

  val routes: MutableMap<String, RouteDefinition> = ConcurrentHashMap()

  fun onMessageTo(target: String, init: RouteDefinition.() -> Unit): RouteDefinition {
    val routeDefinition = RouteDefinition()
    routeDefinition.init()
    routes[target] = routeDefinition
    return routeDefinition
  }

  fun route(message: Message<*>) {
    routes[message.getTarget()]?.getTargets(message)
  }

}
