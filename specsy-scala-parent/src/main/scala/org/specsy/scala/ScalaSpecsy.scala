// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala

import fi.jumi.api.RunVia
import org.specsy.Specsy
import org.specsy.bootstrap.ContextDealer
import org.specsy.core.Closure

import scala.language.implicitConversions

@RunVia(classOf[Specsy])
abstract class ScalaSpecsy {

  private val context = ContextDealer.take()

  /**
   * Declares a child spec.
   */
  def spec(name: String)(body: => Unit) {
    context.spec(name, new ScalaClosure(body))
  }

  protected implicit def String_to_NestedSpec(name: String): NestedSpec = new NestedSpec(name)

  protected class NestedSpec(name: String) {
    /**
     * Declares a child spec.
     */
    def >>(body: => Unit) {
      context.spec(name, new ScalaClosure(body))
    }
  }

  /**
   * Defers the execution of a piece of code until the end of the current spec.
   * All deferred closures will be executed in LIFO order when the current spec exits.
   */
  def defer(block: => Unit) {
    context.defer(new ScalaClosure(block))
  }

  /**
   * Makes all child specs of the current spec able to see each other's side-effects.
   */
  def shareSideEffects() {
    context.shareSideEffects()
  }
}

private class ScalaClosure(closure: => Unit) extends Closure {
  def run() {
    closure
  }
}
