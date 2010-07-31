package net.orfjackal.specsy.runner

import java.lang.Throwable
import scala.collection.immutable.TreeMap
import net.orfjackal.specsy.runner.notification._
import net.orfjackal.specsy.core._

class SuiteMonitor(runner: SuiteRunner, capturer: OutputCapturer) extends SuiteNotifier {
  private var tests = new TreeMap[Path, TestState]

  def results: Map[Path, TestResult] = tests

  def testCount: Int = tests.size

  def passCount: Int = countTestsWhich(_.isPass)

  def failCount: Int = countTestsWhich(_.isFail)

  private def countTestsWhich(p: TestState => Boolean): Int = tests.values.filter(p).size

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

  private class TestState(val path: Path, val name: String, val location: Object) extends TestResult {
    var failures = List[Throwable]()

    private var capture: Capture = null
    var output: String = ""

    def fireTestStarted() {
      capture = capturer.beginCapture()
    }

    def fireTestFinished() {
      capturer.stop()
      output = capture.toString
    }

    def fireFailure(cause: Throwable) {
      failures = cause :: failures
    }

    def isPass = failures.isEmpty

    def isFail = !failures.isEmpty
  }
}
