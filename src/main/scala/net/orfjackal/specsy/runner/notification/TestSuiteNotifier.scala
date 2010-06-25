package net.orfjackal.specsy.runner.notification

trait TestSuiteNotifier {
  def fireSuiteStarted(): Unit

  def fireSuiteFinished(): Unit

  def newTestClassNotifier(testClass: Class[_]): TestClassNotifier
}
