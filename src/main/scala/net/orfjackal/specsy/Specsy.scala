package net.orfjackal.specsy

trait Specsy {
  implicit def newChildSpec(name: String): ChildSpec = new ChildSpec(name)

  class ChildSpec(name: String) {
    def >>(body: => Any) {
      // TODO
      println(name + " >>")
    }
  }
}
