package io.duna.core.eventbus.routing

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.routing.clauses.RouteDefinition
import java.util.concurrent.ConcurrentHashMap

class LocalRouter : Router {

  private val sourceRouteDefinitions = ConcurrentHashMap<String, RouteDefinition.(Message<*>) -> Unit>()
  private val targetRouteDefinitions = ConcurrentHashMap<String, RouteDefinition.(Message<*>) -> Unit>()

  override fun from(source: String, route: RouteDefinition.(Message<*>) -> Unit) {
    sourceRouteDefinitions[source] = route
  }

  override fun to(target: String, route: RouteDefinition.(Message<*>) -> Unit) {
    targetRouteDefinitions[target] = route
  }

  override fun dispatch(message: Message<*>): Future<Message<*>> {
    route(message)
    return Future.completedFuture()
  }

  override fun route(message: Message<*>): Set<String> {
    val route = RouteDefinition()

    if (message.source != null)
      sourceRouteDefinitions[message.source!!]?.invoke(route, message)
    targetRouteDefinitions[message.target]?.invoke(route, message)

    return route.subscribers
  }
}
