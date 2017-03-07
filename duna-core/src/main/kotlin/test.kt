import io.duna.core.eventbus.impl.LocalEventBus

fun main(vararg args: String) {
  val eventBus = LocalEventBus()

  eventBus
    .subscriber<String>("test")
    .register()

  eventBus
    .subscriber<String>("test2")
    .register()

  eventBus
    .subscriber<String>("test3")
    .register()

  eventBus
    .emitter<String>("test")
    .emit("asd")
}
