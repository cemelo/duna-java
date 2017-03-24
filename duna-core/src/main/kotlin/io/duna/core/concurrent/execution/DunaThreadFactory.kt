package io.duna.core.concurrent.execution

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class DunaThreadFactory : ThreadFactory {

  private val threadCounter = AtomicInteger(0)

  override fun newThread(command: Runnable): Thread = DunaThread(command, "duna-${threadCounter.getAndIncrement()}")
}
