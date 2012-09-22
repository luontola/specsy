// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.bootstrap

import org.junit.Test
import fi.jumi.core.runs.{RunId, RunIdSequence, DefaultSuiteNotifier}
import fi.jumi.actors.ActorRef
import fi.jumi.core.runners.TestClassListener
import fi.jumi.api.drivers.TestId
import fi.jumi.core.util.SpyListener
import org.apache.commons.io.output.NullOutputStream
import java.nio.charset.StandardCharsets
import fi.jumi.core.output.OutputCapturer
import org.specsy.core.SpecRun

class ClassSpecTest {

  @Test
  def exceptions_thrown_by_the_root_spec_are_not_wrapped_in_InvocationTargetException() {
    val spy = new SpyListener(classOf[TestClassListener])
    val listener = spy.getListener
    val capturer = new OutputCapturer(new NullOutputStream, new NullOutputStream, StandardCharsets.UTF_8)
    val notifier = new DefaultSuiteNotifier(ActorRef.wrap(listener), new RunIdSequence(), capturer)
    val runner = new SpecRun(new ClassSpec(classOf[DummySpecWhoseRootThrowsAnException]), notifier, null)

    listener.onTestFound(TestId.ROOT, "DummySpecWhoseRootThrowsAnException")
    listener.onRunStarted(new RunId(1))
    listener.onTestStarted(new RunId(1), TestId.ROOT)

    // TODO: this is the thing we are interested in; refactor this test to make it clear
    listener.onFailure(new RunId(1), TestId.ROOT, new AssertionError("exception in root"))

    listener.onTestFinished(new RunId(1), TestId.ROOT)
    listener.onRunFinished(new RunId(1))
    spy.replay()
    runner.run()
    spy.verify()
  }
}

class DummySpecWhoseRootThrowsAnException {
  throw new AssertionError("exception in root")
}
