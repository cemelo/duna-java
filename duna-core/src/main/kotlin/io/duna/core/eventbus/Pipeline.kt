package io.duna.core.eventbus

import java.util.function.Function

interface Pipeline {

  fun addFirst(pipe: Function<Message<*>, Message<*>>): Pipeline

  fun addLast(pipe: Function<Message<*>, Message<*>>): Pipeline

  fun transform(input: Message<*>): Message<*>

  fun isEmpty(): Boolean

}
