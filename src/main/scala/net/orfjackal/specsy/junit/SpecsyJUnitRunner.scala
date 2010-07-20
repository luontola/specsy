package net.orfjackal.specsy.junit

import org.junit.runner._
import net.orfjackal.specsy.core._
import net.orfjackal.specsy.Specsy
import org.junit.runner.notification.Failure
import net.orfjackal.specsy.runner.notification.NullTestClassNotifier

class SpecsyJUnitRunner(testClass: Class[_ <: Specsy]) extends Runner {
  private lazy val result = runSpecs()

  // TODO: reconstruct the spec hierarchy
  private lazy val childDescriptions: List[(Description, SpecRun)] = {
    for (spec <- result.executedSpecs)
    yield (Description.createTestDescription(testClass, spec.name), spec)
  }

  private lazy val suiteDescription = {
    val suite = Description.createSuiteDescription(testClass)
    for ((desc, path) <- childDescriptions) {
      suite.addChild(desc)
    }
    suite
  }

  private def runSpecs(): SpecResult = {
    /*
    val collector = new ResultCollector
    val suite = new SuiteRunner(collector)
    suite.add(testClass)
    suite.run()

    SpecResult(0, 0, Nil, Nil) // TODO
    */
    val runner = new SpecRunner(new NullTestClassNotifier)
    runner.run(c => {
      c.bootstrap(testClass.getSimpleName, {
        ContextDealer.prepare(c)
        testClass.getConstructor().newInstance()
      })
    })
  }

  def run(notifier: org.junit.runner.notification.RunNotifier) {
    for ((desc, spec) <- childDescriptions) {
      notifier.fireTestStarted(desc)
      val failure = findFailure(desc, spec)
      if (failure != null) {
        notifier.fireTestFailure(failure)
      } else {
        notifier.fireTestFinished(desc)
      }
    }
  }

  // XXX: make it easier to convert the results to descriptions
  private def findFailure(desc: Description, currentSpec: SpecRun): Failure = {
    for ((failedSpec, cause) <- result.failures) {
      if (failedSpec == currentSpec) {
        return new Failure(desc, cause)
      }
    }
    return null
  }

  def getDescription = suiteDescription
}
