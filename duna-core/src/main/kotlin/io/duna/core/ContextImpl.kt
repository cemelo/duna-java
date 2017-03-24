package io.duna.core

import java.util.concurrent.ExecutorService

internal class ContextImpl(private val executorService: ExecutorService,
                           private val workerPool: ExecutorService)
  : Context, AbstractMap<String, Any>() {

  private val internalMap = HashMap<String, Any>()

  override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
    get() = internalMap.entries

  override val keys: MutableSet<String>
    get() = internalMap.keys

  override val values: MutableCollection<Any>
    get() = internalMap.values

  override fun execute(block: () -> Unit) {
    if (Context.inDunaThread)
      block.invoke()
    else
      executorService.submit(block)
  }

  override fun executeBlocking(block: () -> Unit) {
    workerPool.submit(block)
  }

  override fun containsKey(key: String): Boolean {
    return super.containsKey(key)
  }

  override fun clear() {
    internalMap.clear()
  }

  override fun get(key: String): Any? {
    return internalMap[key]
  }

  override fun put(key: String, value: Any): Any? {
    return internalMap.put(key, value)
  }

  override fun putAll(from: Map<out String, Any>) {
    internalMap.putAll(from)
  }

  override fun remove(key: String): Any? {
    return internalMap.remove(key)
  }
}
