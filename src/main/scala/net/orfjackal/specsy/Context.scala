package net.orfjackal.specsy

class Context {
  def specify(name: String, body: => Any) {
    body
  }
}
