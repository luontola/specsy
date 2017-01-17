// Copyright © 2010-2017, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import fi.jumi.api.RunVia
import fi.jumi.api.drivers.TestId
import fi.jumi.core.api.TestFile
import fi.jumi.core.results.RunVisitor
import fi.jumi.core.testbench.TestBench
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
import org.junit.Test
import org.mockito.Mockito.{mock, verifyZeroInteractions}

class SpecsyJumiDriverTest {

  @Test
  def runs_tests_using_Jumi() {
    val testClass = classOf[DummySpec]

    val results = new TestBench().run(testClass)

    val testName = results.getTestName(TestFile.fromClass(testClass), TestId.ROOT)
    assertThat(testName, is("DummySpec"))
  }

  @Test
  def will_not_run_abstract_classes() {
    val testClass = classOf[AbstractDummySpec]

    val results = new TestBench().run(testClass)

    val visitor = mock(classOf[RunVisitor])
    results.visitAllRuns(visitor)
    verifyZeroInteractions(visitor)
  }
}

@RunVia(classOf[Specsy])
class DummySpec {
}

@RunVia(classOf[Specsy])
abstract class AbstractDummySpec {
}
