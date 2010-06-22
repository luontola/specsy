package net.orfjackal.specsy.runner

import org.junit.runner.notification.RunNotifier
import org.junit.runner._
import net.orfjackal.specsy._
import scala.collection.mutable.Buffer

class SpecsyJUnitRunner(testClass: Class[Specsy]) extends Runner {
  def run(notifier: RunNotifier) {
    val result = new Result()
    notifier.addListener(result.createListener)
    notifier.fireTestRunStarted(getDescription)

    var toBeExecuted = Buffer[Path](Path())
    while (toBeExecuted.length > 0) {
      val pathToExecute = toBeExecuted.remove(0)

      val desc = Description.createTestDescription(testClass, "path: " + pathToExecute)
      notifier.fireTestStarted(desc)

      val result = executePath(pathToExecute)

      notifier.fireTestFinished(desc)

      toBeExecuted.appendAll(result.postponedPaths)
    }

    notifier.fireTestRunFinished(result)
  }

  private def executePath(path: Path) = {
    val context = new Context(path)
    executeWithContext(context)
    context
  }

  private def executeWithContext(context: Context): Unit = {
    context.specify(testClass.getSimpleName, {
      ContextDealer.prepare(context)
      testClass.getConstructor().newInstance()
    })
  }

  def getDescription: Description = {
    Description.createSuiteDescription(testClass)
  }
}
