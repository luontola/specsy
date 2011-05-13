// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.core

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.Spec
import net.orfjackal.specsy.runner.SuiteMonitor

class SpecClassRunnerTest {
  @Test
  def exceptions_thrown_by_the_root_spec_are_not_wrapped_in_InvocationTargetException() {
    val unusedCapturer = new OutputCapturer(null, null)
    val monitor = new SuiteMonitor(null, unusedCapturer)
    val runner = new SpecClassRunner(classOf[DummySpecWhoseRootThrowsAnException], monitor)

    runner.run()

    val exception = monitor.results(Path.Root).failures.head
    assertThat(exception, is(classOf[AssertionError]))
    assertThat(exception.getMessage, is("exception in root"))
  }
}

class DummySpecWhoseRootThrowsAnException extends Spec {
  throw new AssertionError("exception in root")
}
