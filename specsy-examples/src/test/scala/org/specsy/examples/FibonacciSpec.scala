// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples

import org.junit.runner.RunWith
import org.specsy.scala._
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._

@RunWith(classOf[Specsy])
class FibonacciSpec extends Spec {
  val sequenceLength = 10
  val fib = new Fibonacci().sequence(sequenceLength)
  assertThat(fib.length, is(sequenceLength))

  "The first two Fibonacci numbers are 0 and 1" >> {
    assertThat(fib(0), is(0))
    assertThat(fib(1), is(1))
  }
  "Each remaining number is the sum of the previous two" >> {
    for (i <- 2 until fib.length) {
      assertThat(fib(i), is(fib(i - 1) + fib(i - 2)))
    }
  }
}
