package io.duna.core.bus.routing.clauses

import java.util.*

class MulticastDefinition {

  val targets: MutableList<String> = LinkedList()

  fun to(vararg targets: String) {
    this.targets.addAll(targets)
  }

}
