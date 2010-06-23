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
      c.specify("root", {})
    })
    assertThat(result.runCount, is(1))
  }

  @Test
  def there_are_as_many_runs_as_leaf_specs() {
    val result = runner.run(c => {
      c.specify("root", {
        c.specify("child A", {})
        c.specify("child B", {})
      })
    })
    assertThat(result.runCount, is(2))
  }

  // TODO: report pass & fail
  // TODO: report failure stack traces (and shorten them)
  // TODO: capture test output
}
