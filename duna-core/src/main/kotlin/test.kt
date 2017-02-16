import org.eclipse.collections.impl.multimap.set.sorted.SynchronizedPutTreeSortedSetMultimap

/**
 * Created by carlos on 09/02/17.
 */

fun main(vararg args: String) {
//  launch(CommonPool) {
//    call()
//  }

  val x = SynchronizedPutTreeSortedSetMultimap.newMultimap<Int, String>();
  x.put(1, "a")
  x.put(1, "b")
  x.put(1, "c")
  x.put(5, "d")
  x.put(6, "d")
  x.put(3, "d")
  x.put(2, "e")
  x.put(8, "d")
  x.put(7, "d")
  x.put(7, "e")
  x.put(7, "f")
  x.put(4, "d")

  x.removeAll(1)

  println(x.keySet())
  println(x.keyValuePairsView())
  x.remove(7, "e")
  x.remove(2, "e")

  println(x.keyValuePairsView())
}
