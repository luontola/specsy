package net.orfjackal.specsy

trait Specsy {
  private val context = ContextDealer.take()

  protected implicit def specify(name: String): NestedSpec = new NestedSpec(name)

  protected class NestedSpec(name: String) {
    def >>(body: => Any) {
      context.specify(name, body)
    }
  }
}
