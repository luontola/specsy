// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import fi.jumi.api.RunVia
import fi.jumi.api.drivers.TestId
import fi.jumi.core.api.TestFile
import fi.jumi.core.testbench.TestBench
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
import org.junit.Assert.assertTrue
import org.junit.Test

class SpecsyJumiDriverTest {

  @Test
  def runs_tests_using_Jumi() {
    val testClass = classOf[DummySpec]

    val results = new TestBench().run(testClass)

    val testName = results.getTestName(TestFile.fromClass(testClass), TestId.ROOT)
    assertThat(testName, is("DummySpec"))
  }

  @Test
  def version_number() {
    val version = new Specsy().getVersion
    assertTrue(version, version.matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?|<local-build>"))
  }
}

@RunVia(classOf[Specsy])
class DummySpec {
}
