// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit

import org.junit.runner._
import org.specsy.core._
import org.specsy.runner._
import org.junit.runner.notification.Failure

class SpecsyJUnitRunner(testClass: Class[_]) extends Runner {
  private lazy val results: Map[Path, TestResult] = runSpecs()
  private lazy val converter = new ResultToDescriptionConverter(testClass, results)

  private def runSpecs() = {
    val realOut = System.out
    val realErr = System.err
    val captureOutput = System.getProperty("specsy.captureOutput", "false") == "true"

    val runner = new SuiteRunner
    val capturer = new OutputCapturer(realOut, realErr)
    val monitor = new SuiteMonitor(runner, capturer)

    try {
      if (captureOutput) {
        System.setOut(capturer.capturedOut)
        System.setErr(capturer.capturedErr)
        Console.setOut(capturer.capturedOut)
        Console.setErr(capturer.capturedErr)
      }

      runner.submitTestRun(new SpecClassRunner(testClass, monitor))
      runner.await()
    } finally {
      if (captureOutput) {
        System.setOut(realOut)
        System.setErr(realErr)
        Console.setOut(realOut)
        Console.setErr(realErr)
      }
    }

    monitor.results
  }

  def run(notifier: org.junit.runner.notification.RunNotifier) {
    for (path <- converter.pathsInOrder) {
      val desc = converter.descriptionForPath(path)
      val result = converter.resultForPath(path)

      notifier.fireTestStarted(desc)
      printTestOutput(result)
      if (result.failures.isEmpty) {
        notifier.fireTestFinished(desc)
      } else {
        notifier.fireTestFailure(new Failure(desc, result.failures.head))
      }
    }
  }

  private def printTestOutput(result: TestResult) {
    // HACK: IntelliJ IDEA tries to notice that which test prints what text,
    // but it's too sensitive to the timing. If the test runs quickly, IDEA
    // will believe that the text was printed during another test. The only
    // reliable solution appears to be CTR4J.
    Thread.`yield`()
    System.out.print(result.output)
    System.out.flush()
    Thread.`yield`()
  }

  def getDescription: Description = converter.topLevelDescription
}
