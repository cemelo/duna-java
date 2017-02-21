import io.duna.core.internal.eventbus.LocalEventBus
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.yield

val context = newSingleThreadContext("single")

suspend fun run() {
  val eventBus = LocalEventBus()

  eventBus.inbound<Int>("test")
    .addListener {
      println("Source: ${it.source}, Target: ${it.target}")
      println(it.body)
    }

  eventBus.inbound<Int>("test2")
    .addListener {
      println(it.target)
      println(it.body)
    }

  launch(context) {
    eventBus.outbound<Int>("test")
      .withBody(1)
      .emit()

    yield()

    eventBus.outbound<Int>("test")
      .withBody(2)
      .emit()

    yield()

    eventBus.outbound<Int>("test")
      .withBody(3)
      .emit()
  }

  launch(context) {
    println("Yelded")
  }

  launch(context) {
    eventBus.outbound<Int>("test2")
      .withBody(4)
      .emit()

    yield()

    eventBus.outbound<Int>("test2")
      .withBody(5)
      .emit()

    yield()

    eventBus.outbound<Int>("test2")
      .withBody(6)
      .emit()
  }
}

fun main(vararg args: String){
  runBlocking(context) {
    run()
  }
}
