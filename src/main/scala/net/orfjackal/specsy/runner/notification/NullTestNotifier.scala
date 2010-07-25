package net.orfjackal.specsy.runner.notification

class NullTestNotifier extends TestNotifier {
  def fireFailure(cause: Throwable) {}

  def fireTestFinished() {}
}
