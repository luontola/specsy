package net.orfjackal.specsy.core

import scala.collection.mutable._
import net.orfjackal.specsy.runner.notification._
import net.orfjackal.specsy.Specsy

class SpecRunner(testClass: Class[_ <: Specsy], notifier: SuiteNotifier) extends Runnable {
  def run() {
    run(c => {
      c.bootstrap(testClass.getName, {
        ContextDealer.prepare(c)
        testClass.getConstructor().newInstance() // TODO: may throw exceptions
      })
    })
  }

  def run(spec: Context => Unit) {
    val queue = ListBuffer[Path]()
    queue.append(Path.Root)
    val executedSpecs = ListBuffer[SpecRun]()

    while (queue.length > 0) {
      val pathToExecute = queue.remove(0)
      val c = executePath(pathToExecute, spec)
      executedSpecs.append(c.executedSpec)
      // TODO: move the responsibility of scheduling postponed executions to some higher layer
      queue.appendAll(c.postponedPaths)
    }
  }

  private def executePath(path: Path, spec: (Context) => Unit): Context = {
    val c = new Context(path, notifier)
    executeSafely(c, spec)
    c
  }

  private def executeSafely(c: Context, spec: (Context) => Unit): Unit = {
    try {
      spec(c)
    } catch {
      case e =>
        e.printStackTrace
        throw new RuntimeException("internal error", e)
    }
  }
}
