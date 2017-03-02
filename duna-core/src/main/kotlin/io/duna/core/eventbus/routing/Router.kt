package io.duna.core.eventbus.routing

import io.duna.core.eventbus.Message
import io.duna.core.eventbus.routing.clauses.RouteDefinition

/**
 * Routes messages to events.
 */
interface Router {

  /**
   * Creates a new routing rule based on the source of the message.
   *
   * @param source the event that triggered the message.
   * @param init   a function with routing rules.
   * @return the new route definition.
   */
  fun from(source: String, init: RouteDefinition.() -> Unit): RouteDefinition

  /**
   * Creates a new routing rule based on the target of the message.
   *
   * @param target the message target event.
   * @param init   a function with routing rules.
   * @return the new route definition.
   */
  fun to(target: String, init: RouteDefinition.() -> Unit): RouteDefinition

  /**
   * Dispatches a message to subscribers in this node according to the
   * route definitions.
   *
   * @param message the message to be delivered.
   */
  fun route(message: Message<*>)

}
