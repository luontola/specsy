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
    val runner = new SuiteRunner
    val capturer = new OutputCapturer(System.out, System.err)
    // TODO: install with System.setOut/setErr
    val monitor = new SuiteMonitor(runner, capturer)
    runner.submitTestRun(new SpecClassRunner(testClass, monitor))
    runner.await()
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
