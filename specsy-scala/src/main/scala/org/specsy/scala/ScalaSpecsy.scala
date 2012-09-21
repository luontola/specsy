// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala

import org.specsy.core.ContextDealer

trait ScalaSpecsy {

  private val context = ContextDealer.take()

  /**
   * Makes all child specs of the current spec able to see each other's side-effects.
   */
  def shareSideEffects() {
    context.shareSideEffects()
  }

  /**
   * Defers the execution of a piece of code until the end of the current spec.
   * All deferred closures will be executed in LIFO order when the current spec exits.
   */
  def defer(body: => Unit) {
    context.defer(body)
  }

  protected implicit def stringToNestedSpec(name: String): NestedSpec = new NestedSpec(name)

  protected class NestedSpec(name: String) {
    /**
     * Declares a child spec.
     */
    def >>(body: => Unit) {
      context.specify(name, body)
    }
  }
}
