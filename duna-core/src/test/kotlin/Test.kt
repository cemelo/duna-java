import io.duna.core.internal.eventbus.LocalEventBus
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking

val context = newFixedThreadPoolContext(2, "test")

suspend fun run() {
  val eventBus = LocalEventBus()

  eventBus.inbound<Int>("test")
    .addListener {
      println("Source: ${it.source}, Target: ${it.target}")
      println(it.body)
    }

  eventBus.inbound<Int>("test2")
    .asFlowable()
    .subscribe(::println)

  val v1 = launch(context) {
    eventBus.outbound<Int>("test")
      .setBody(1)
      .emit()

    eventBus.outbound<Int>("test")
      .setBody(2)
      .emit()

    eventBus.outbound<Int>("test")
      .setBody(3)
      .emit()
  }

  launch(context) {
    println("Yelded")
  }

  val v2 = launch(context) {
    eventBus.outbound<Int>("test2")
      .setBody(4)
      .emit()

    eventBus.outbound<Int>("test2")
      .setBody(5)
      .emit()

    eventBus.outbound<Int>("test2")
      .setBody(6)
      .emit()
  }

  v1.join()
  v2.join()
}

fun main(vararg args: String){
  runBlocking(context) {
    run()
  }
}
