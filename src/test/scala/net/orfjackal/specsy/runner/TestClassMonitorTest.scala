package net.orfjackal.specsy.runner

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit._
import net.orfjackal.specsy.core.Path
import net.orfjackal.specsy.Specsy
import java.util.concurrent.LinkedBlockingQueue

class TestClassMonitorTest {
  val runQueue = new LinkedBlockingQueue[Runnable]
  val monitor = new TestClassMonitor(runQueue)

  @Test
  def empty_test_class() {
    assertThat("test count", monitor.testCount, is(0))
    assertThat("pass count", monitor.passCount, is(0))
    assertThat("fail count", monitor.failCount, is(0))

    assertThat("test names", monitor.testNames, is(Map[Path, String]()))
  }

  @Test
  def one_passing_test() {
    monitor.fireTestFound(Path(), "root", classOf[DummySpec])
    val t = monitor.fireTestStarted(Path())
    t.fireTestFinished()

    assertThat("test count", monitor.testCount, is(1))
    assertThat("pass count", monitor.passCount, is(1))
    assertThat("fail count", monitor.failCount, is(0))

    assertThat("test names", monitor.testNames, is(Map(
      Path() -> "root"
      )))
  }

  @Test
  def one_failing_test() {
    monitor.fireTestFound(Path(), "root", classOf[DummySpec])
    val t = monitor.fireTestStarted(Path())
    t.fireFailure(new RuntimeException)
    t.fireTestFinished()

    assertThat("test count", monitor.testCount, is(1))
    assertThat("pass count", monitor.passCount, is(0))
    assertThat("fail count", monitor.failCount, is(1))

    assertThat("test names", monitor.testNames, is(Map(
      Path() -> "root"
      )))
  }

  @Test
  def nested_tests() {
    monitor.fireTestFound(Path(), "root", classOf[DummySpec])
    val root = monitor.fireTestStarted(Path())

    monitor.fireTestFound(Path(0), "child A", null)
    val childA = monitor.fireTestStarted(Path(0))
    childA.fireTestFinished()

    root.fireTestFinished()

    assertThat("test count", monitor.testCount, is(2))
    assertThat("pass count", monitor.passCount, is(2))
    assertThat("fail count", monitor.failCount, is(0))

    assertThat("test names", monitor.testNames, is(Map(
      Path() -> "root",
      Path(0) -> "child A"
      )))
  }

  @Test
  def sibling_tests() {
    {
      monitor.fireTestFound(Path(), "root", classOf[DummySpec])
      val root = monitor.fireTestStarted(Path())

      monitor.fireTestFound(Path(0), "child A", null)
      val childA = monitor.fireTestStarted(Path(0))
      childA.fireTestFinished()

      monitor.fireTestFound(Path(1), "child B", null)

      root.fireTestFinished()
    }
    {
      monitor.fireTestFound(Path(), "root", classOf[DummySpec])
      val root = monitor.fireTestStarted(Path())

      monitor.fireTestFound(Path(0), "child A", null)

      monitor.fireTestFound(Path(1), "child B", null)
      val childB = monitor.fireTestStarted(Path(1))
      childB.fireTestFinished()

      root.fireTestFinished()
    }

    assertThat("test count", monitor.testCount, is(3))
    assertThat("pass count", monitor.passCount, is(3))
    assertThat("fail count", monitor.failCount, is(0))

    assertThat("test names", monitor.testNames, is(Map(
      Path() -> "root",
      Path(0) -> "child A",
      Path(1) -> "child B"
      )))
  }

  @Test
  def submitted_test_runs_are_added_to_the_run_queue() {
    val testRun = new Runnable {
      def run {}
    }
    assertThat("queue size before", runQueue.size, is(0))

    monitor.submitTestRun(testRun)

    assertThat("queue size after", runQueue.size, is(1))
    assertThat("submitted item", runQueue.take, is(testRun))
  }

  private class DummySpec extends Specsy {
  }
}

