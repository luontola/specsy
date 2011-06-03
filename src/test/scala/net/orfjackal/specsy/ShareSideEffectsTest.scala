// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import collection.mutable.Buffer
import net.orfjackal.specsy.core._
import net.orfjackal.specsy.runner._

class ShareSideEffectsTest {
  val spy = Buffer[String]()

  private def runSpec(spec: Context => Unit): Any = {
    val runner = new SuiteRunner
    val unusedCapturer = new OutputCapturer(null, null)
    val monitor = new SuiteMonitor(runner, unusedCapturer)
    runner.submitTestRun(new SpecRun(spec, monitor))
    runner.await()
  }

  @Test
  def children_are_affected() {
    runSpec(c => {
      c.bootstrap("root", {
        c.shareSideEffects()
        spy.append("root")

        c.specify("child A", {
          spy.append("A")
        })
        c.specify("child A", {
          spy.append("B")
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "B")))
  }

  @Test
  def nested_children_are_affected() {
    runSpec(c => {
      c.bootstrap("root", {
        c.shareSideEffects()
        spy.append("root")

        c.specify("child A", {
          spy.append("A")

          c.specify("child AA", {
            spy.append("AA")
          })
          c.specify("child AB", {
            spy.append("AB")
          })
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "AA", "AB")))
  }

  @Test
  def siblings_are_not_affected() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")

        c.specify("child A", {
          c.shareSideEffects()
          spy.append("A")
        })
        c.specify("child B", {
          spy.append("B")
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "root", "B")))
  }
}
