package net.orfjackal.specsy.internal

case class SpecResult(passCount: Int, failCount: Int, failures: List[(Spec, Throwable)]) {
  assert(failCount == failures.length, "Expected " + failCount + " failures but was " + failures)

  def runCount: Int = passCount + failCount
}
