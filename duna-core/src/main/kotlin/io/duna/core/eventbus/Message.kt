package io.duna.core.eventbus

interface Message<out T> {

  fun getSource(): String

  fun getTarget(): String

  fun getResponseEvent(): String

  fun getHeaders(): Headers

  fun getBody(): T

  fun header(key: String): Set<String> = getHeaders()[key].toHashSet()
}
