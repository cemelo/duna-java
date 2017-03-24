package io.duna.core.bus.impl

import io.duna.core.bus.Headers
import io.duna.core.bus.Message
import org.eclipse.collections.impl.factory.Multimaps

internal data class MessageImpl<out T>(override val source: String? = null,
                                       override val target: String,
                                       override val responseEvent: String? = null,
                                       override val headers: Headers = Multimaps.mutable.list.empty<String, String> (),
                                       override val attachment: T? = null,
                                       override val cause: Throwable? = null)
  : Message<T> {

  companion object {
    internal val EMPTY_MESSAGE = MessageImpl<Any>(target = "")
  }
}
