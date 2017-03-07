package io.duna.core.eventbus.routing.clauses

import io.duna.core.eventbus.event.Subscriber

class RouteChoice {

  var target: String? = null

  fun to(target: String): Unit {
    this.target = target
  }

  fun to(target: Subscriber<*>): Unit {
    this.target = target.name
  }

}
