package net.orfjackal.specsy.core

import scala.collection.mutable._
import net.orfjackal.specsy.runner.notification.TestClassNotifier

class SpecRunner(notifier: TestClassNotifier) {
  private var passCount = 0
  private var failCount = 0
  private var failures = List[(SpecRun, Throwable)]()

  def run(spec: Context => Unit): SpecResult = {
    val queue = ListBuffer[Path]()
    queue.append(Path.Root)
    val executedSpecs = ListBuffer[SpecRun]()

    notifier.fireTestClassStarted()
    while (queue.length > 0) {
      val pathToExecute = queue.remove(0)
      val c = executePath(pathToExecute, spec)
      executedSpecs.append(c.executedSpec)
      // TODO: move the responsibility of scheduling postponed executions to some higher layer
      queue.appendAll(c.postponedPaths)
    }
    notifier.fireTestClassFinished()

    // TODO: move the responsibility of collecting results to the notifier implementation
    SpecResult(passCount, failCount, executedSpecs.toList, failures)
  }

  private def executePath(path: Path, spec: (Context) => Unit): Context = {
    val c = new Context(path, notifier.newTestRunNotifier())
    executeSafely(c, spec)
    saveResults(c)
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

  private def saveResults(c: Context): Unit = {
    if (c.failures.isEmpty) {
      passCount += 1
    } else {
      failCount += 1
      failures = failures ::: c.failures
    }
  }
}
