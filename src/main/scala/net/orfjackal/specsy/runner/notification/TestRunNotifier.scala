package net.orfjackal.specsy.runner.notification

import net.orfjackal.specsy.core.Path

trait TestRunNotifier {
  def fireTestStarted(path: Path, name: String): Unit

  def fireTestFinished(path: Path): Unit
}
