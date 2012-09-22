// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import org.specsy.core._
import collection.mutable.Buffer
import fi.jumi.core.testbench.{StubDriverFinder, TestBench}
import fi.jumi.api.drivers.{TestId, SuiteNotifier, Driver}
import java.util.concurrent.Executor
import fi.jumi.core.results.{NullRunVisitor, SuiteEventDemuxer}
import fi.jumi.core.runs.RunId

class DeferBlocksTest {

  val spy = Buffer[String]()

  private def runSpec(spec: Context => Unit): SuiteEventDemuxer = {
    val bench = new TestBench()
    bench.setDriverFinder(new StubDriverFinder(new Driver {
      def findTests(testClass: Class[_], notifier: SuiteNotifier, executor: Executor) {
        executor.execute(new SpecRun(new SpecAdapter(spec), notifier, executor))
      }
    }))
    bench.run(getClass)
  }

  // Defer blocks in Specsy behave the same way as in the Go programming language.
  // See http://golang.org/doc/effective_go.html#defer

  @Test
  def code_in_defer_blocks_is_executed_after_the_spec() {
    runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          spy.append("defer")
        }
        spy.append("spec")
      })
    })

    assertThat(spy, is(Buffer("spec", "defer")))
  }

  @Test
  def defer_blocks_in_nested_specs_are_executed_before_their_parents() {
    runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          spy.append("root defer")
        }

        c.specify("child", {
          c.defer {
            spy.append("child defer")
          }
        })
      })
    })

    assertThat(spy, is(Buffer("child defer", "root defer")))
  }

  @Test
  def multiple_defer_blocks_are_executed_in_LIFO_order() {
    runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          spy.append("first defer")
        }
        c.defer {
          spy.append("second defer")
        }
      })
    })

    assertThat(spy, is(Buffer("second defer", "first defer")))
  }

  @Test
  def all_defer_blocks_are_executed_despite_exceptions_and_all_exceptions_are_reported() {
    // XXX: We cannot throw the exception directly in the block passed to defer, because
    // exception throwing has a type Nothing, which can be passed as a `Closure`,
    // which in turn results in the the eager `defer(Closure)` instead of `defer(=>Unit)`.
    def fail(message: String) {
      throw new Throwable(message)
    }

    val results = runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          fail("first defer")
        }
        c.defer {
          fail("second defer")
        }
        fail("root")
      })
    })

    val failures = Buffer[Throwable]()
    results.visitAllRuns(new NullRunVisitor {
      override def onFailure(runId: RunId, testClass: String, testId: TestId, cause: Throwable) {
        failures.append(cause)
      }
    })
    assertThat(failures.size, is(3))
    assertThat(failures(0).getMessage, is("root"))
    assertThat(failures(1).getMessage, is("second defer"))
    assertThat(failures(2).getMessage, is("first defer"))
  }

  @Test
  def defer_blocks_are_executed_despite_exceptions_in_nested_specs() {
    runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          spy.append("root defer")
        }

        c.specify("child", {
          throw new AssertionError()
        })
      })
    })

    assertThat(spy, is(Buffer("root defer")))
  }
}
