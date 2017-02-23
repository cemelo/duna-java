package io.duna.core.eventbus.routing.clauses

import java.util.*

class MulticastDefinition {

  val targets: MutableList<String> = LinkedList()

  fun to(vararg targets: String) {
    this.targets.addAll(targets)
  }

}
