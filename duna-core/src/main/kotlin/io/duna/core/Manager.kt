package io.duna.core

import io.duna.core.concurrent.Future
import io.duna.core.bus.OldEventBus

interface Manager {

  fun eventBus(): OldEventBus

  fun shutdown(): Future<Void>

}
