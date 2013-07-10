// Copyright © 2010-2013, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import fi.jumi.api.drivers.TestId
import fi.jumi.core.results.NullRunVisitor
import collection.mutable
import util.TestHelpers
import fi.jumi.core.api.{RunId, StackTrace, TestFile}

class DeferBlocksTest extends TestHelpers {

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

    assertSpyContains("spec", "defer")
  }

  @Test
  def defer_blocks_in_nested_specs_are_executed_before_their_parents() {
    runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          spy.append("root defer")
        }

        c.spec("child", {
          c.defer {
            spy.append("child defer")
          }
        })
      })
    })

    assertSpyContains("child defer", "root defer")
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

    assertSpyContains("second defer", "first defer")
  }

  @Test
  def all_defer_blocks_are_executed_despite_exceptions_and_all_exceptions_are_reported() {
    val results = runSpec(c => {
      c.bootstrap("root", {
        c.defer {
          throw new Throwable("first defer")
        }
        c.defer {
          throw new Throwable("second defer")
        }
        throw new Throwable("root")
      })
    })

    val failures = mutable.Buffer[StackTrace]()
    results.visitAllRuns(new NullRunVisitor {
      override def onFailure(runId: RunId, testFile: TestFile, testId: TestId, cause: StackTrace) {
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

        c.spec("child", {
          throw new Throwable("child")
        })
      })
    })

    assertSpyContains("root defer")
  }
}
