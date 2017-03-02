package io.duna.core.eventbus.impl

import io.duna.core.eventbus.Message
import io.duna.core.eventbus.Pipeline
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.function.Function

internal class PipelineImpl : Pipeline {

  private val transforms: Deque<Function<*, *>> = ConcurrentLinkedDeque()

  override fun <V, R> addFirst(pipe: Function<Message<V>, Message<R>>): Pipeline {
    transforms.addFirst(pipe)
    return this
  }

  override fun <V, R> addLast(pipe: Function<Message<V>, Message<R>>): Pipeline {
    transforms.addLast(pipe as Function<*, *>)
    return this
  }

  @Suppress("UNCHECKED_CAST")
  override fun <V, R> transform(input: Message<V>): Message<R> {
    var result: Message<*> = input

    transforms.forEach {
      result = (it as Function<Message<*>, Message<*>>).apply(result)
    }

    return result as Message<R>
  }

  override fun isEmpty(): Boolean = transforms.isEmpty()
}
