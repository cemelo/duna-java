package io.duna.core.eventbus.impl

import io.duna.core.eventbus.Headers
import io.duna.core.eventbus.Message
import org.eclipse.collections.impl.factory.Multimaps

internal data class MessageImpl<out T>(private val source: String? = null,
                                       private val target: String,
                                       private val responseEvent: String? = null,
                                       private val headers: Headers = Multimaps.mutable.list.empty(),
                                       private val attachment: T? = null,
                                       internal val messageType: MessageType = MessageType.UNICAST)
  : Message<T> {

  override fun getSource(): String? = source

  override fun getTarget(): String = target

  override fun getResponseEvent(): String? = responseEvent

  override fun getHeaders(): Headers = headers

  override fun getAttachment(): T? = attachment

}
