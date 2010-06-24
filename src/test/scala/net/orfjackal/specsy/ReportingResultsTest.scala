package net.orfjackal.specsy

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.internal._

class ReportingResultsTest {
  val runner = new SpecRunner

  @Test
  def reports_the_total_number_of_specs_run() {
    val result = runner.run(c => {
      c.bootstrap("root", {})
    })
    assertThat(result.runCount, is(1))
  }

  @Test
  def there_are_as_many_runs_as_leaf_specs() {
    val result = runner.run(c => {
      c.bootstrap("root", {
        c.specify("child A", {})
        c.specify("child B", {})
      })
    })
    assertThat(result.runCount, is(2))
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
    assertThat(result.passCount, is(2))
    assertThat(result.failCount, is(1))
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
    assertThat(result.passCount, is(2))
    assertThat(result.failCount, is(1))
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

    val failureA = result.failures(0)
    val failureB = result.failures(1)
    assertThat(failureA._1.name, is("child A"))
    assertThat(failureA._2.getMessage, is("failure A"))
    assertThat(failureB._1.name, is("child B"))
    assertThat(failureB._2.getMessage, is("failure B"))
  }

  // TODO: shorten failure stack traces
  // TODO: capture test output
}
