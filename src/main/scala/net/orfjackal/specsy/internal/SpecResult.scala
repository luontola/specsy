package net.orfjackal.specsy.internal

case class SpecResult(
        passCount: Int,
        failCount: Int,
        executedSpecs: List[SpecRun],
        failures: List[(SpecRun, Throwable)]) {
  assert(failCount == failures.length, "Expected " + failCount + " failures but was " + failures)

  def runCount: Int = passCount + failCount
}
