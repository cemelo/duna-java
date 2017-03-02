package io.duna.core.eventbus

import java.util.function.Function

interface Pipeline {

  fun <V, R> addFirst(pipe: Function<Message<V>, Message<R>>): Pipeline

  fun <V, R> addLast(pipe: Function<Message<V>, Message<R>>): Pipeline

  fun <V, R> transform(input: Message<V>): Message<R>

  fun isEmpty(): Boolean

}
