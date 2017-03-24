package io.duna.core.concurrent.execution

import java.util.concurrent.ExecutorService

class DunaThread(block: Runnable,
                 name: String) : Thread(block, "duna-$name") {

  internal lateinit var executorService: ExecutorService

  companion object {
    @JvmStatic
    fun currentThread(): DunaThread? {
      if (Thread.currentThread() is DunaThread)
        return Thread.currentThread() as DunaThread

      return null
    }
  }
}
