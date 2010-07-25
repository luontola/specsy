package net.orfjackal.specsy.runner.notification

trait TestNotifier {
  def fireFailure(cause: Throwable)

  def fireTestFinished()
}
