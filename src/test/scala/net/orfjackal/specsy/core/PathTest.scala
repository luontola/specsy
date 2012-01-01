// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.core

import org.junit._
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import scala.util.Random

class PathTest {
  @Test
  def paths_are_sorted_in_natural_order() {
    val naturalOrder = List(
      Path(),
      Path(0),
      Path(0, 0),
      Path(0, 1),
      Path(1),
      Path(1, 0),
      Path(1, 1))

    val randomized = Random.shuffle(naturalOrder)
    assertThat(randomized.sorted, is(naturalOrder))
  }
}
