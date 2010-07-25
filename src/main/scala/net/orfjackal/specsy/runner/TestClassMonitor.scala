package net.orfjackal.specsy.runner

import net.orfjackal.specsy.core.Path
import java.lang.Throwable
import scala.collection.immutable.TreeMap
import net.orfjackal.specsy.runner.notification._

class TestClassMonitor(runner: SuiteRunner) extends SuiteNotifier {
  private var tests = new TreeMap[Path, TestState]

  def testCount: Int = tests.size

  def passCount: Int = countTestsWhich(_.isPass)

  def failCount: Int = countTestsWhich(_.isFail)

  private def countTestsWhich(p: TestState => Boolean): Int = tests.values.filter(p).size

  def testNames: Map[Path, String] = tests.mapValues(_.name)

  def fireTestFound(path: Path, name: String, location: Object) {
    if (!tests.contains(path)) {
      tests = tests.updated(path, new TestState(path, name, location))
    }
  }

  def submitTestRun(testRun: Runnable) {
    runner.submitTestRun(testRun)
  }

  def fireTestStarted(path: Path): TestNotifier = {
    val test = tests(path)
    test.fireTestStarted()
    new MyTestNotifier(test)
  }

  private class MyTestNotifier(test: TestState) extends TestNotifier {
    def fireFailure(cause: Throwable) {
      test.fireFailure(cause)
    }

    def fireTestFinished() {
      test.fireTestFinished()
    }
  }

  private class TestState(val path: Path, val name: String, val location: Object) {
    private var failures = List[Throwable]()

    def fireTestStarted() {
    }

    def fireTestFinished() {
    }

    def fireFailure(cause: Throwable) {
      failures = cause :: failures
    }

    def isPass = failures.isEmpty

    def isFail = !failures.isEmpty
  }
}
