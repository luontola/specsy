// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import collection.mutable.Buffer
import org.specsy.core._
import fi.jumi.core.testbench.{StubDriverFinder, TestBench}
import fi.jumi.api.drivers.{SuiteNotifier, Driver}
import java.util.concurrent.Executor

class ExecutionModelTest {
  val spy = Buffer[String]()

  private def runSpec(spec: Context => Unit): Any = {
    val bench = new TestBench()
    bench.setDriverFinder(new StubDriverFinder(new Driver {
      def findTests(testClass: Class[_], notifier: SuiteNotifier, executor: Executor) {
        executor.execute(new SpecRun(spec, notifier, executor))
      }
    }))
    bench.run(getClass)
  }

  @Test
  def there_is_one_root_spec() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")
      })
    })

    assertThat(spy, is(Buffer("root")))
  }

  @Test
  def the_root_spec_can_contain_multiple_nested_child_specs() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")

        c.specify("child A", {
          spy.append("A")

          c.specify("child AA", {
            spy.append("AA")
          })
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "AA")))
  }

  @Test
  def the_specs_are_executed_one_branch_at_a_time_until_all_are_executed() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")

        c.specify("child A", {
          spy.append("A")
        })
        c.specify("child B", {
          spy.append("B")
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "root", "B")))
  }

  @Test
  def in_single_threaded_mode_the_child_specs_are_executed_in_declaration_order() {
    runSpec(c => {
      c.bootstrap("root", {
        c.specify("child A", {
          spy.append("A")
        })
        c.specify("child B", {
          spy.append("B")
        })
        c.specify("child C", {
          spy.append("C")
        })
        c.specify("child D", {
          spy.append("D")
        })
      })
    })

    assertThat(spy, is(Buffer("A", "B", "C", "D")))
  }

  @Test
  def variables_declared_inside_specs_are_isolated_from_the_side_effects_of_sibling_specs() {
    runSpec(c => {
      c.bootstrap("root", {
        var i = 0

        c.specify("child A", {
          i += 1
          spy.append("A" + i)
        })
        c.specify("child B", {
          i += 1
          spy.append("B" + i)
        })
      })
    })

    assertThat(spy, is(Buffer("A1", "B1")))
  }

  @Test
  def child_specs_are_executed_immediately_where_they_are_declared() {
    runSpec(c => {
      c.bootstrap("root", {
        var i = 0

        c.specify("child A", {
          spy.append("A" + i)
        })
        i += 1

        c.specify("child B", {
          spy.append("B" + i)
        })
      })
    })

    assertThat(spy, is(Buffer("A0", "B1")))
  }
}
