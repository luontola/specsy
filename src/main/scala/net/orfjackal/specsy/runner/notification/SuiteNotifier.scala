package net.orfjackal.specsy.runner.notification

import net.orfjackal.specsy.core.Path

trait SuiteNotifier {
  def fireTestFound(path: Path, name: String, location: Object)

  def submitTestRun(testRun: Runnable)

  def fireTestStarted(path: Path): TestNotifier
}
