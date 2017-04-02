package io.duna.core.concurrent.task

interface Task : Runnable {

  val context: TaskContext

}
