package net.orfjackal.specsy

import net.orfjackal.specsy.core.ContextDealer

trait Spec {
  private val context = ContextDealer.take()

  protected implicit def specify(name: String): NestedSpec = new NestedSpec(name)

  protected class NestedSpec(name: String) {
    def >>(body: => Unit) {
      context.specify(name, body)
    }
  }
}