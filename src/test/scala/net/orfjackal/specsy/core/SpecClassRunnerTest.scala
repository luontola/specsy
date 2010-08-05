package net.orfjackal.specsy.core

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.Spec
import net.orfjackal.specsy.runner.notification._
import java.lang.Throwable
import scala.collection.mutable.Buffer

class SpecClassRunnerTest {
  @Test
  def exceptions_thrown_by_the_root_spec_are_not_wrapped_in_InvocationTargetException() {
    val notifier = new FailureLoggingNotifier
    val runner = new SpecClassRunner(classOf[DummySpecWhoseRootThrowsAnException], notifier)

    runner.run()

    val exception = notifier.failures.head
    assertThat(exception, is(classOf[AssertionError]))
    assertThat(exception.getMessage, is("exception in root"))
  }

  private class FailureLoggingNotifier extends NullSuiteNotifier with TestNotifier {
    val failures = Buffer[Throwable]()

    override def fireTestStarted(path: Path) = this

    def fireFailure(cause: Throwable) {
      failures.append(cause)
    }

    def fireTestFinished() {}
  }
}

class DummySpecWhoseRootThrowsAnException extends Spec {
  throw new AssertionError("exception in root")
}
