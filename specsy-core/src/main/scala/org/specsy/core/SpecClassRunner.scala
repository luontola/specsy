// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import org.specsy.runner.notification._
import java.lang.reflect.InvocationTargetException

class SpecClassRunner(testClass: Class[_], notifier: SuiteNotifier) extends Runnable {
  def run() {
    runSpec(c => {
      // TODO: pass testClass to the runner so that the test's location will be the test class, and not this closure
      c.bootstrap(testClass.getName, {
        ContextDealer.prepare(c)
        try {
          testClass.getConstructor().newInstance()
        } catch {
          case e: InvocationTargetException => throw e.getTargetException
        }
      })
    })
  }

  private def runSpec(spec: Context => Unit) {
    val runner = new SpecRun(spec, notifier)
    runner.run()
  }
}
