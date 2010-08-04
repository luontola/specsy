package net.orfjackal.specsy.core

import net.orfjackal.specsy.runner.notification._
import net.orfjackal.specsy.Spec

class SpecClassRunner(testClass: Class[_ <: Spec], notifier: SuiteNotifier) extends Runnable {
  def run() {
    runSpec(c => {
      // TODO: pass testClass to the runner so that the test's location will be the test class, and not this closure
      c.bootstrap(testClass.getName, {
        ContextDealer.prepare(c)
        testClass.getConstructor().newInstance() // TODO: may throw exceptions - unwrap InvocationTargetException to improve error messages
      })
    })
  }

  private def runSpec(spec: Context => Unit) {
    val runner = new SpecRun(spec, notifier)
    runner.run()
  }
}
