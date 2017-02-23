package io.duna.core.eventbus.queue

import java.util.function.BiConsumer
import java.util.function.Supplier

interface MessageQueue<T> {

  fun poll(): T

  fun offer(item: T)

  fun close()

  fun isClosed(): Boolean

  fun isBlocking(): Boolean

  fun setProducer(producer: Supplier<out T>): MessageQueue<T>

  fun setConsumer(consumer: BiConsumer<MessageQueue<T>, in T>): MessageQueue<T>

  fun setBlocking(blocking: Boolean): MessageQueue<T>
}
