package io.duna.core.eventbus.routing

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.routing.clauses.RouteDefinition

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
