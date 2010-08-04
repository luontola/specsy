package net.orfjackal.specsy.runner

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit._
import net.orfjackal.specsy.Spec
import net.orfjackal.specsy.core._

class SuiteMonitorTest {
  val unusedRunner = null
  val unusedCapturer = new OutputCapturer(System.out, System.err)
  val monitor = new SuiteMonitor(unusedRunner, unusedCapturer)

  private def testNames: Map[Path, String] = monitor.results.mapValues(_.name)

  @Test
  def empty_test_class() {
    assertThat("test count", monitor.testCount, is(0))
    assertThat("pass count", monitor.passCount, is(0))
    assertThat("fail count", monitor.failCount, is(0))

    assertThat("test names", testNames, is(Map[Path, String]()))
  }

  @Test
  def one_passing_test() {
    monitor.fireTestFound(Path(), "root", classOf[DummySpec])
    val t = monitor.fireTestStarted(Path())
    t.fireTestFinished()

    assertThat("test count", monitor.testCount, is(1))
    assertThat("pass count", monitor.passCount, is(1))
    assertThat("fail count", monitor.failCount, is(0))

    assertThat("test names", testNames, is(Map(
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

    assertThat("test names", testNames, is(Map(
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

    assertThat("test names", testNames, is(Map(
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

    assertThat("test names", testNames, is(Map(
      Path() -> "root",
      Path(0) -> "child A",
      Path(1) -> "child B"
      )))
  }

  private class DummySpec extends Spec {
  }
}

