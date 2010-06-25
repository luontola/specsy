package net.orfjackal.specsy.runner.notification

import net.orfjackal.specsy.internal.Path

trait TestRunNotifier {
  def fireTestStarted(path: Path, name: String): Unit

  def fireTestFinished(path: Path): Unit
}
