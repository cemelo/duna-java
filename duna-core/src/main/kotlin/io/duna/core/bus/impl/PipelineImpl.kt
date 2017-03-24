package io.duna.core.bus.impl

import io.duna.core.bus.Message
import io.duna.core.bus.Pipeline
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.function.Function

internal class PipelineImpl : Pipeline {

  private val transforms: Deque<Function<Message<*>, Message<*>>> = ConcurrentLinkedDeque()

  override fun addFirst(pipe: Function<Message<*>, Message<*>>): Pipeline {
    transforms.addFirst(pipe)
    return this
  }

  override fun addLast(pipe: Function<Message<*>, Message<*>>): Pipeline {
    transforms.addLast(pipe)
    return this
  }

  override fun transform(input: Message<*>): Message<*> {
    var result = input

    transforms.forEach {
      result = it.apply(result)
    }

    return result
  }

  override fun isEmpty(): Boolean = transforms.isEmpty()
}
