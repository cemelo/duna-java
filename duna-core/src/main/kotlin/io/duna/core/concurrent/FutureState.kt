package io.duna.core.concurrent

internal enum class FutureState {
  NEW, COMPLETED, FAILED, CANCELLED
}
