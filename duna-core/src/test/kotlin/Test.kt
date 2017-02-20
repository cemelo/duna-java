import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlin.concurrent.thread

fun main(vararg args: String) {
  val flowable = Flowable.create<Int>({ emitter ->
    println("Inside create.")
    if (emitter.isCancelled) println("Cancelled")
    var a = 0

    thread {
      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      Thread.sleep(1000)

      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      Thread.sleep(1000)

      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onNext(a++)
      emitter.onComplete()
    }

  }, BackpressureStrategy.BUFFER)
    .publish()
    .refCount()

  flowable.subscribe(::println)
  flowable.take(1).subscribe { println("A: $it") }
  Thread.sleep(1200)
  flowable.take(1).subscribe { println("B: $it") }
  Thread.sleep(1000)
}
