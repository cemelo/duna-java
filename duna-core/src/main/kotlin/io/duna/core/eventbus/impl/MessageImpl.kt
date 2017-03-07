package io.duna.core.eventbus.impl

import io.duna.core.eventbus.Headers
import io.duna.core.eventbus.Message
import org.eclipse.collections.impl.factory.Multimaps

internal data class MessageImpl<out T>(override val source: String? = null,
                                       override val target: String,
                                       override val responseEvent: String? = null,
                                       override val headers: Headers = Multimaps.mutable.list.empty(),
                                       override val attachment: T? = null,
                                       override val cause: Throwable? = null,
                                       internal val messageType: MessageType = MessageType.UNICAST)
  : Message<T>
