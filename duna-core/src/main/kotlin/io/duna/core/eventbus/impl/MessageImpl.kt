package io.duna.core.eventbus.impl

import io.duna.core.eventbus.Headers
import io.duna.core.eventbus.Message

internal data class MessageImpl<out T>(private val source: String,
                                       private val target: String,
                                       private val responseEvent: String,
                                       private val headers: Headers,
                                       private val attachment: T)
  : Message<T> {

  override fun getSource(): String = source

  override fun getTarget(): String = target

  override fun getResponseEvent(): String = responseEvent

  override fun getHeaders(): Headers = headers

  override fun getAttachment(): T = attachment

}
