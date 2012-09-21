// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples

import org.specsy.scala._
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import fi.jumi.api.RunVia

@RunVia(classOf[Specsy])
class ShareSideEffectsExampleSpec extends Spec {
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
