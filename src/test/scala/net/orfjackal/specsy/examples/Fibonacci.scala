package net.orfjackal.specsy.examples

class Fibonacci {
  private var n0 = 0
  private var n1 = 1

  def sequence(count: Int): Seq[Int] = {
    for (i <- 1 to count) yield next()
  }

  def next(): Int = {
    val next = n0
    n0 = n1
    n1 = n1 + next
    next
  }
}
