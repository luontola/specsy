// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.runner

import java.lang.Throwable
import scala.collection.immutable.TreeMap
import net.orfjackal.specsy.runner.notification._
import net.orfjackal.specsy.core._
import scala.collection.mutable.Buffer

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
    private val _failures = Buffer[Throwable]()

    def failures = _failures.toList

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
      _failures.append(cause)
    }

    def isPass = _failures.isEmpty

    def isFail = !_failures.isEmpty
  }
}
