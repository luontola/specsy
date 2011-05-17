// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples

import org.junit.runner.RunWith
import net.orfjackal.specsy._
import org.junit.Assert._
import org.hamcrest.CoreMatchers._

@RunWith(classOf[Specsy])
class ShareSideEffectsExampleSpec extends Spec {
  var i = 0

  shareSideEffects()
  "One" >> {
    i += 1
    assertThat(i, is(1))
  }
  "Two" >> {
    i += 1
    assertThat(i, is(2))
  }
  "Three" >> {
    i += 1
    assertThat(i, is(3))
  }
}
