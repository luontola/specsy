package net.orfjackal.specsy.junit

import org.junit._
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.core.Path
import org.junit.runner.Description
import net.orfjackal.specsy.runner._

class ResultToDescriptionConverterTest {
  private val testClass = classOf[DummySpec]
  private class DummySpec {}

  val unusedRunner = null
  val monitor = new SuiteMonitor(unusedRunner)

  def toDescription(results: Map[Path, TestResult]): Description = {
    val converter = new ResultToDescriptionConverter(testClass, results)
    converter.topLevelDescription
  }

  @Test
  def spec_with_only_root() {
    val expected = Description.createSuiteDescription(testClass)

    monitor.fireTestFound(Path(), testClass.getName, testClass)

    val actual = toDescription(monitor.results)
    assertThat(actual, is(expected))
  }

  @Test
  def spec_with_children() {
    val expected = Description.createSuiteDescription(testClass)
    expected.addChild(Description.createTestDescription(testClass, "child A"))
    expected.addChild(Description.createTestDescription(testClass, "child B"))

    monitor.fireTestFound(Path(), testClass.getName, testClass)
    monitor.fireTestFound(Path(0), "child A", null)
    monitor.fireTestFound(Path(1), "child B", null)

    val actual = toDescription(monitor.results)
    assertThat(actual.getChildren, is(expected.getChildren))
    assertThat(actual, is(expected))
  }

  @Test
  def spec_with_nested_children() {
    val expected = Description.createSuiteDescription(testClass)
    val childA = Description.createSuiteDescription("child A") // non-leafs must be suites, or else IDEA will not show them correctly
    expected.addChild(childA)
    childA.addChild(Description.createTestDescription(testClass, "child AA"))

    monitor.fireTestFound(Path(), testClass.getName, testClass)
    monitor.fireTestFound(Path(0), "child A", null)
    monitor.fireTestFound(Path(0, 0), "child AA", null)

    val actual = toDescription(monitor.results)
    assertThat(actual.getChildren.get(0).getChildren, is(expected.getChildren.get(0).getChildren))
    assertThat(actual.getChildren, is(expected.getChildren))
    assertThat(actual, is(expected))
  }
}
