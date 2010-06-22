package net.orfjackal.specsy.runner

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit.runner._
import org.junit.runners.Suite
import org.junit.runner.notification._
import scala.collection.mutable.Buffer
import org.junit.internal.builders.JUnit4Builder

class JUnitRunnerLearningTest {
  val events = Buffer[String]()

  @Test
  def running_individual_test_classes() {
    val core = new JUnitCore
    core.addListener(new SpyRunListener)
    val result = core.run(classOf[DummyTest1], classOf[DummyTest2])

    assertThat(events, is(Buffer(
      "testRunStarted",
      "testStarted test1(net.orfjackal.specsy.runner.DummyTest1)",
      "testFinished test1(net.orfjackal.specsy.runner.DummyTest1)",
      "testStarted test2(net.orfjackal.specsy.runner.DummyTest2)",
      "testFinished test2(net.orfjackal.specsy.runner.DummyTest2)",
      "testStarted test3(net.orfjackal.specsy.runner.DummyTest2)",
      "testFinished test3(net.orfjackal.specsy.runner.DummyTest2)",
      "testRunFinished")))
    assertThat(result.getRunCount, is(3))
  }

  @Test
  def running_test_suites() {
    val core = new JUnitCore
    core.addListener(new SpyRunListener)
    val result = core.run(classOf[DummySuite])

    // the suite itself is not seen by the RunListener
    // - the output is exactly the same as when running individual test classes
    assertThat(events, is(Buffer(
      "testRunStarted",
      "testStarted test1(net.orfjackal.specsy.runner.DummyTest1)",
      "testFinished test1(net.orfjackal.specsy.runner.DummyTest1)",
      "testStarted test2(net.orfjackal.specsy.runner.DummyTest2)",
      "testFinished test2(net.orfjackal.specsy.runner.DummyTest2)",
      "testStarted test3(net.orfjackal.specsy.runner.DummyTest2)",
      "testFinished test3(net.orfjackal.specsy.runner.DummyTest2)",
      "testRunFinished")))
    assertThat(result.getRunCount, is(3))
  }

  @Test
  def values_returned_by_suite_runners() {
    val suiteRunner = new Suite(classOf[DummySuite], new JUnit4Builder)
    assertThat(suiteRunner.testCount, is(3))

    val desc = suiteRunner.getDescription
    assertThat(desc.getDisplayName, is("net.orfjackal.specsy.runner.DummySuite"))

    // contains the full tree of suites and tests
    val children = desc.getChildren
    assertThat(children.size, is(2))
    assertThat(children.get(0).getDisplayName, is("net.orfjackal.specsy.runner.DummyTest1"))
    assertThat(children.get(0).getChildren.size, is(1))
    assertThat(children.get(1).getDisplayName, is("net.orfjackal.specsy.runner.DummyTest2"))
    assertThat(children.get(1).getChildren.size, is(2))
  }


  class SpyRunListener extends RunListener {
    override def testIgnored(description: Description) {
      events.append("testIgnored " + description.getDisplayName)
    }

    override def testAssumptionFailure(failure: Failure) {
      events.append("testAssumptionFailure " + failure.getDescription.getDisplayName)
    }

    override def testFailure(failure: Failure) {
      events.append("testFailure " + failure.getDescription.getDisplayName)
    }

    override def testFinished(description: Description) {
      events.append("testFinished " + description.getDisplayName)
    }

    override def testStarted(description: Description) {
      println("-- " + description + "\t" + description.getChildren)
      events.append("testStarted " + description.getDisplayName)
    }

    override def testRunFinished(result: Result) {
      events.append("testRunFinished")
    }

    override def testRunStarted(description: Description) {
      events.append("testRunStarted")
    }
  }
}

@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(classOf[DummyTest1], classOf[DummyTest2]))
class DummySuite

class DummyTest1 {
  @Test def test1() {}
}

class DummyTest2 {
  @Test def test2() {}

  @Test def test3() {}
}
