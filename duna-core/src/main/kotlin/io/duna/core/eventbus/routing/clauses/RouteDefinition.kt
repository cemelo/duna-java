package io.duna.core.eventbus.routing.clauses

import io.duna.core.eventbus.Message
import java.util.*

class RouteDefinition {

  var redirect: (RouteChoice.(Message<*>) -> Unit)? = null
  val multicastDefinition: MulticastDefinition = MulticastDefinition()

  fun choose(redirect: RouteChoice.(Message<*>) -> Unit) {
    this.redirect = redirect
  }

  fun multicast(init: MulticastDefinition.() -> Unit): MulticastDefinition {
    multicastDefinition.init()
    return multicastDefinition
  }

  fun getTargets(message: Message<*>): List<String> {
    val routeChoice = RouteChoice()
    redirect?.invoke(routeChoice, message)

    val result = LinkedList<String>()
    result.add(routeChoice.target!!)
    result.addAll(multicastDefinition.targets)

    return result
  }
}
