package io.duna.core.bus

class UnknownSubscriberException(msg: String? = null, cause: Throwable? = null)
  : RuntimeException(msg, cause)
