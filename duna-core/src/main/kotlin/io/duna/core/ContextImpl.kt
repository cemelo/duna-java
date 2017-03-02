package io.duna.core

internal class ContextImpl : Context, AbstractMap<String, Any>() {

  private val internalMap = HashMap<String, Any>()

  override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
    get() = internalMap.entries

  override val keys: MutableSet<String>
    get() = internalMap.keys

  override val values: MutableCollection<Any>
    get() = internalMap.values

  override fun manager(): Manager {
    TODO("not implemented")
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
