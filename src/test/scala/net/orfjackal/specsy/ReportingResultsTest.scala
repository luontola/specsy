package net.orfjackal.specsy

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.core._
import net.orfjackal.specsy.runner.SuiteMonitor

class ReportingResultsTest {
  val unusedRunner = null
  val monitor = new SuiteMonitor(unusedRunner)
  val runner = new SpecRunner(null, monitor)

  @Test
  def reports_the_total_number_of_specs_run() {
    val result = runner.run(c => {
      c.bootstrap("root", {})
    })

    assertThat("test count", monitor.testCount, is(1))
  }

  @Test
  def there_are_as_many_runs_as_there_are_leaf_specs() {
    var runCount = 0

    val result = runner.run(c => {
      c.bootstrap("root", {
        runCount += 1

        c.specify("child A", {})
        c.specify("child B", {})
      })
    })

    assertThat("run count", runCount, is(2))
    assertThat("test count", monitor.testCount, is(3))
  }

  @Test
  def reports_passed_and_failed_runs() {
    val result = runner.run(c => {
      c.bootstrap("root", {
        c.specify("child A", {})
        c.specify("child B", {})
        c.specify("child C", {
          throw new AssertionError("a failure")
        })
      })
    })

    assertThat("pass count", monitor.passCount, is(3))
    assertThat("fail count", monitor.failCount, is(1))
  }

  @Test
  def the_rest_of_the_siblings_are_executed_even_when_the_first_sibling_fails() {
    val result = runner.run(c => {
      c.bootstrap("root", {
        c.specify("child A", {
          throw new AssertionError("a failure")
        })
        c.specify("child B", {})
        c.specify("child C", {})
      })
    })

    assertThat("pass count", monitor.passCount, is(3))
    assertThat("fail count", monitor.failCount, is(1))
  }

  @Test
  def reports_the_specs_and_exceptions_that_caused_a_failure() {
    val result = runner.run(c => {
      c.bootstrap("root", {
        c.specify("child A", {
          throw new AssertionError("failure A")
        })
        c.specify("child B", {
          throw new AssertionError("failure B")
        })
      })
    })

    val childA = monitor.results(Path(0))
    val childB = monitor.results(Path(1))
    assertThat(childA.failures.head.getMessage, is("failure A"))
    assertThat(childB.failures.head.getMessage, is("failure B"))
  }

  // TODO: shorten failure stack traces
  // TODO: capture test output
}
