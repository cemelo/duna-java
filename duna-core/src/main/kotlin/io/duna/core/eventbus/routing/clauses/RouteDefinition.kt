package io.duna.core.eventbus.routing.clauses

class RouteDefinition {

  internal val subscribers = HashSet<String>()

  fun deliverTo(target: String) {
    subscribers.add(target)
  }

  fun multicastTo(vararg targets: String) {
    subscribers.addAll(targets)
  }

}
