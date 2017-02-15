import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * Created by carlos on 09/02/17.
 */

fun main(vararg args: String) {
//  launch(CommonPool) {
//    call()
//  }

  val runnable = object : Runnable {
    override fun run() {
      println(this.toString() + " " + Thread.currentThread())
    }
  }

  val t1 = Thread(runnable)
  val t2 = Thread(runnable)

  t1.start()
  t2.start()
}

suspend fun call() {
  val f = launch(CommonPool) {
    delay(1000L)
    println("World!")
  }

  print("Hello, ")
  f.join()
}
