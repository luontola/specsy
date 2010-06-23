package net.orfjackal.specsy.internal

import scala.collection.mutable.Buffer

class SpecRunner {
  private var passCount = 0
  private var failCount = 0
  private var failures = List[(SpecRun, Throwable)]()

  def run(spec: Context => Unit): SpecResult = {
    var queue = Buffer[Path]()
    queue.append(Path())
    var executedSpecs = Buffer[SpecRun]()

    while (queue.length > 0) {
      val pathToExecute = queue.remove(0)
      val c = executePath(pathToExecute, spec)
      executedSpecs.append(c.executedSpec)
      queue.appendAll(c.postponedPaths)
    }

    SpecResult(passCount, failCount, executedSpecs.toList, failures)
  }

  private def executePath(path: Path, spec: (Context) => Unit): Context = {
    val c = new Context(path)
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
        throw new RuntimeException("Internal error", e)
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
