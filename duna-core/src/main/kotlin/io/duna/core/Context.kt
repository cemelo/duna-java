package io.duna.core

interface Context : MutableMap<String, Any> {

  fun manager(): Manager

  companion object {
    fun currentContext(): Context? {
      return null
    }
  }
}
