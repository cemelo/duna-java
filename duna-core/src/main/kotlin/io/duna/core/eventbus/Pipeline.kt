package io.duna.core.eventbus

interface Pipeline {

  fun <V, R> addFirst(function: Pipe<V, R>): Pipeline

  fun <V, R> addLast(function: Pipe<V, R>): Pipeline

}
