package net.orfjackal.specsy.runner

import net.orfjackal.specsy.core._
import net.orfjackal.specsy.runner.notification.TestSuiteNotifier

class SuiteRunner(notifier: TestSuiteNotifier) {
  private var testClasses = Set[Class[_]]()

  def add(testClass: Class[_]) {
    testClasses += testClass
  }

  def run() {
    notifier.fireSuiteStarted()
    for (testClass <- testClasses) {
      runTestClass(testClass)
    }
    notifier.fireSuiteFinished()
  }

  private def runTestClass(testClass: Class[_]) {
    val runner = new SpecRunner(notifier.newTestClassNotifier(testClass))
    runner.run(c => {
      c.bootstrap(testClass.getSimpleName, {
        ContextDealer.prepare(c)
        testClass.getConstructor().newInstance()
      })
    })
  }
}
