package net.orfjackal.specsy.runner.notification

trait TestClassNotifier {
  def fireTestClassStarted(): Unit

  def fireTestClassFinished(): Unit

  def newTestRunNotifier(): TestRunNotifier
}
