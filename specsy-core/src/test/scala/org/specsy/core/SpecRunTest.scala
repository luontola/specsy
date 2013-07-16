// Copyright © 2010-2013, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import org.junit.Test
import org.specsy.bootstrap.ClassSpec

class SpecRunTest {

  @Test
  def has_custom_toString_for_better_logging_in_Jumi() {
    assertThat(new SpecRun(new ClassSpec(getClass), null, null).toString,
      is("org.specsy.core.SpecRun(org.specsy.bootstrap.ClassSpec(org.specsy.core.SpecRunTest), Path(TestId()))"))
  }
}
