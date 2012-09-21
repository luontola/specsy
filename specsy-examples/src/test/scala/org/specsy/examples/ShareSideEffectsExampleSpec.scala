// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples

import fi.jumi.api.RunVia
import org.specsy.Specsy
import org.specsy.scala.ScalaSpecsy
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._

@RunVia(classOf[Specsy])
class ShareSideEffectsExampleSpec extends ScalaSpecsy {
  var counter = 0

  // Without the call to `shareSideEffects()` the value of `counter` would be `1`
  // in the asserts of each of the following child specs.
  shareSideEffects()
  "One" >> {
    counter += 1
    assertThat(counter, is(1))
  }
  "Two" >> {
    counter += 1
    assertThat(counter, is(2))
  }
  "Three" >> {
    counter += 1
    assertThat(counter, is(3))
  }
}
