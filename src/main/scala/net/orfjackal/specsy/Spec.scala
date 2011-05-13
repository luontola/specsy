// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy

import net.orfjackal.specsy.core.ContextDealer

trait Spec {
  private val context = ContextDealer.take()

  def defer(body: => Unit) {
    context.defer(body)
  }

  protected implicit def stringToNestedSpec(name: String): NestedSpec = new NestedSpec(name)

  protected class NestedSpec(name: String) {
    def >>(body: => Unit) {
      context.specify(name, body)
    }
  }
}
