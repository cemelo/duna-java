package io.duna.core

import io.duna.core.concurrent.Future
import io.duna.core.bus.EventBus

interface Manager {

  fun eventBus(): EventBus

  fun shutdown(): Future<Void>

}
