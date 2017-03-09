package io.duna.core.eventbus.routing

import io.duna.core.concurrent.Future
import io.duna.core.eventbus.Message
import io.duna.core.eventbus.routing.clauses.RouteDefinition

/**
 * Routes messages to events.
 */
interface Router {

  /**
   * Creates a new routing rule.
   *
   * @param route a function with routing rules.
   * @return the new dispatch definition.
   */
  fun create(name: String, route: RouteDefinition.() -> Unit)

  /**
   *
   */
  fun remove(name: String)

  /**
   * Dispatches a message to subscribers in this node according to the
   * dispatch definitions.
   *
   * @param message the message to be delivered.
   */
  fun dispatch(message: Message<*>): Future<Message<*>>

  fun route(message: Message<*>): Set<String>
}
