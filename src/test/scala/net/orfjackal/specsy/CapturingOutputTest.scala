// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import net.orfjackal.specsy.core._
import net.orfjackal.specsy.runner._

class CapturingOutputTest {
  val runner = new SuiteRunner
  val capturer = new OutputCapturer(System.out, System.err)
  val out = capturer.capturedOut
  val monitor = new SuiteMonitor(runner, capturer)

  private def runSpec(spec: Context => Unit): Any = {
    runner.submitTestRun(new SpecRun(spec, monitor))
    runner.await()
  }

  @Test
  def captures_the_stdout_of_a_spec() {
    runSpec(c => {
      c.bootstrap("root", {
        out.print("printed in test")
      })
    })

    val result = monitor.results(Path.Root)
    assertThat(result.output, is("printed in test"))
  }

  @Test
  def the_output_of_nested_specs_is_separated() {
    runSpec(c => {
      c.bootstrap("root", {
        out.print("out-root")
        c.specify("child A", {
          out.print("out-A")
        })
        c.specify("child A", {
          out.print("out-B")
        })
      })
    })

    assertThat(monitor.results(Path.Root).output, is("out-root"))
    assertThat(monitor.results(Path(0)).output, is("out-A"))
    assertThat(monitor.results(Path(1)).output, is("out-B"))
  }
}
