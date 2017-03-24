package io.duna.core.bus.routing

import io.duna.core.concurrent.Future
import io.duna.core.bus.Message
import io.duna.core.bus.routing.clauses.RouteDefinition

class LocalRouter : Router {

  override fun create(name: String, route: RouteDefinition.() -> Unit) {
    TODO("not implemented")
  }

  override fun remove(name: String) {
    TODO("not implemented")
  }

  override fun dispatch(message: Message<*>): Future<Message<*>> {
    TODO("not implemented")
  }

  override fun route(message: Message<*>): Set<String> {
    TODO("not implemented")
  }

}
