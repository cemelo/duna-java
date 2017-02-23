package io.duna.core.eventbus.routing.clauses

class RouteChoice {

  var target: String? = null

  fun to(target: String): Unit {
    this.target = target
  }

}
