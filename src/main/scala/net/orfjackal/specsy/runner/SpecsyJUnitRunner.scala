package net.orfjackal.specsy.runner

import org.junit.runner._
import scala.collection.mutable.Buffer
import net.orfjackal.specsy.internal._
import net.orfjackal.specsy.Specsy
import org.junit.runner.notification._

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
    val runner = new SpecRunner
    runner.run(c => {
      c.bootstrap(testClass.getSimpleName, {
        ContextDealer.prepare(c)
        testClass.getConstructor().newInstance()
      })
    })
  }

  def run(notifier: RunNotifier) {
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
