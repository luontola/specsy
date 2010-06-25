package net.orfjackal.specsy.core

case class SpecResult(
        passCount: Int,
        failCount: Int,
        executedSpecs: List[SpecRun],
        failures: List[(SpecRun, Throwable)]) {
  assert(failCount == failures.length, "expected " + failCount + " failures, but was " + failures)

  def runCount: Int = passCount + failCount
}
