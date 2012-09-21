// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import fi.jumi.core.testbench.TestBench
import fi.jumi.api.drivers.TestId
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
import fi.jumi.api.RunVia

class SpecsyJumiDriverTest {

  @Test
  def runs_tests_using_Jumi() {
    val testClass = classOf[DummySpec]

    val results = new TestBench().run(testClass)

    val testName = results.getTestName(testClass.getName, TestId.ROOT)
    assertThat(testName, is("DummySpec"))
  }
}

@RunVia(classOf[Specsy])
class DummySpec {
}
