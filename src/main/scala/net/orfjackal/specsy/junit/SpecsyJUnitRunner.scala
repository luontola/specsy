package net.orfjackal.specsy.junit

import org.junit.runner._
import net.orfjackal.specsy.core._
import net.orfjackal.specsy.Specsy
import net.orfjackal.specsy.runner._
import org.junit.runner.notification.Failure

class SpecsyJUnitRunner(testClass: Class[_ <: Specsy]) extends Runner {
  private lazy val results: Map[Path, TestResult] = runSpecs()
  private lazy val converter = new ResultToDescriptionConverter(testClass, results)

  private def runSpecs() = {
    // TODO: use the suite runner to run the tests
    val suiteRunner = new SuiteRunner
    val monitor = new SuiteMonitor(suiteRunner)
    val runner = new SpecRunner(monitor)

    val r = runner.run(c => {
      c.bootstrap(testClass.getSimpleName, {
        ContextDealer.prepare(c)
        testClass.getConstructor().newInstance() // TODO: may throw exceptions
      })
    })
    monitor.results
  }

  def run(notifier: org.junit.runner.notification.RunNotifier) {
    for (path <- converter.pathsInOrder) {
      val desc = converter.descriptionForPath(path)
      val result = converter.resultForPath(path)

      notifier.fireTestStarted(desc)
      if (result.failures.isEmpty) {
        notifier.fireTestFinished(desc)
      } else {
        notifier.fireTestFailure(new Failure(desc, result.failures.head))
      }
    }
  }

  def getDescription: Description = converter.topLevelDescription
}
