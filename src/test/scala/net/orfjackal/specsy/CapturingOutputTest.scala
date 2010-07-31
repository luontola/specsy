package net.orfjackal.specsy

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
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

  // TODO: nested specs
  // TODO: JUnit integration
}
