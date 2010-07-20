package net.orfjackal.specsy.core

import org.junit._
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
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
