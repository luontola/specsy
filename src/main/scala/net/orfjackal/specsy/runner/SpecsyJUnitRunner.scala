package net.orfjackal.specsy.runner

import org.junit.runner.notification.RunNotifier
import org.junit.runner._
import net.orfjackal.specsy.Specsy

class SpecsyJUnitRunner(testClass: Class[Specsy]) extends Runner {
  def run(notifier: RunNotifier) {
    val result = new Result()
    notifier.addListener(result.createListener)
    notifier.fireTestRunStarted(getDescription)

    // TODO: run the specs

    testClass.getConstructor().newInstance()

    val test1 = Description.createTestDescription(testClass, "test1")
    notifier.fireTestStarted(test1)
    notifier.fireTestFinished(test1)

    val test2 = Description.createTestDescription(testClass, "test2")
    notifier.fireTestStarted(test2)
    notifier.fireTestFinished(test2)

    notifier.fireTestRunFinished(result)
  }

  def getDescription: Description = {
    Description.createSuiteDescription(testClass)
  }
}
