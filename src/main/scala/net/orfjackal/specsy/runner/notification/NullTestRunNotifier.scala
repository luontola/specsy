package net.orfjackal.specsy.runner.notification

import net.orfjackal.specsy.core.Path

class NullTestRunNotifier extends TestRunNotifier {
  def fireTestStarted(path: Path, name: String) {}

  def fireTestFinished(path: Path) {}
}
