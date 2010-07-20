package net.orfjackal.specsy.runner.notification

class NullTestClassNotifier extends TestClassNotifier {
  def fireTestClassStarted() {}

  def fireTestClassFinished() {}

  def newTestRunNotifier() = new NullTestRunNotifier
}
