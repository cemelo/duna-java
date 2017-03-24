package io.duna.core.bus.routing.clauses

import io.duna.core.bus.event.Subscriber

class RouteChoice {

  var target: String? = null

  fun to(target: String): Unit {
    this.target = target
  }

  fun to(target: Subscriber<*>): Unit {
    this.target = target.event
  }

}
