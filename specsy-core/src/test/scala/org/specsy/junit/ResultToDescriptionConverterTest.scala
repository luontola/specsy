// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit

import org.junit._
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import org.junit.runner.Description
import org.specsy.runner._
import org.specsy.core._

class ResultToDescriptionConverterTest {
  private val testClass = classOf[DummySpec]

  private class DummySpec {}

  val unusedRunner = null
  val unusedCapturer = new OutputCapturer(null, null)
  val monitor = new SuiteMonitor(unusedRunner, unusedCapturer)

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

  @Test
  def descriptions_are_in_spec_declaration_order() {
    monitor.fireTestFound(Path(), testClass.getName, testClass)
    monitor.fireTestFound(Path(0), "child A", null)
    monitor.fireTestFound(Path(1), "child B", null)
    monitor.fireTestFound(Path(2), "child C", null)
    monitor.fireTestFound(Path(3), "child D", null)
    monitor.fireTestFound(Path(4), "child E", null)

    val children = toDescription(monitor.results).getChildren
    assertThat(children.get(0).getMethodName, is("child A"))
    assertThat(children.get(1).getMethodName, is("child B"))
    assertThat(children.get(2).getMethodName, is("child C"))
    assertThat(children.get(3).getMethodName, is("child D"))
    assertThat(children.get(4).getMethodName, is("child E"))
  }
}
