import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

val quotes = arrayOf(
  "Software is only one interpretation of the reality of the problem it is solving. - Michael A. Jackson",
  "Computer Science is embarrassed by the computer. - Alan Perlis",
  "Computer science is no more about computers than astronomy is about telescopes. - Edsger Dijkstra"
)

fun main(vararg args: String) {
  val shuttingDown = AtomicBoolean(false)
  var x = 0

  thread {
    println("Running A")
    while (!shuttingDown.get()) {
      x++
    }
    println("A: $x")
  }

  thread {
    println("I'm executing")
    Thread.sleep(1000)
    println("I just woke up")
    println("B1: $x")
    shuttingDown.set(true)
    println("B2: $x")
  }

}
