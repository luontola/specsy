package net.orfjackal.specsy.runner.notification

import java.lang.Class

class NullTestSuiteNotifier extends TestSuiteNotifier {
  def fireSuiteStarted() {}

  def fireSuiteFinished() {}

  def newTestClassNotifier(testClass: Class[_]) = new NullTestClassNotifier
}
