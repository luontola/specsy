package net.orfjackal.specsy.runner

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit._
import scala.collection.mutable.Buffer

class SuiteRunnerTest {
  val runner = new SuiteRunner

  @Test
  def executes_submitted_test_runs() {
    var runCount = 0

    runner.submitTestRun(new Runnable {
      def run() {
        runCount += 1
      }
    })
    runner.await()

    assertThat("run count", runCount, is(1))
  }

  @Test
  def submitted_test_runs_are_executed_after_the_current_run_is_finished() {
    val events = Buffer[String]()

    val run2 = new Runnable {
      def run() {
        events.append("run submitted")
      }
    }
    val run1 = new Runnable {
      def run() {
        events.append("before submit")
        runner.submitTestRun(run2)
        events.append("after submit")
      }
    }

    runner.submitTestRun(run1)
    runner.await()

    assertThat(events, is(Buffer("before submit", "after submit", "run submitted")))
  }
}
