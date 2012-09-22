// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import java.lang.reflect.InvocationTargetException
import java.util.concurrent.Executor
import fi.jumi.api.drivers.SuiteNotifier

class SpecClassRunner(testClass: Class[_], notifier: SuiteNotifier, executor: Executor) extends Runnable {
  def run() {
    runSpec(c => {
      c.bootstrap(testClass.getSimpleName, new Closure {
        def run() {
          ContextDealer.prepare(c)
          try {
            testClass.getConstructor().newInstance()
          } catch {
            case e: InvocationTargetException => throw e.getTargetException
          }
        }
      })
    })
  }

  private def runSpec(spec: Context => Unit) {
    val runner = new SpecRun(spec, notifier, executor)
    runner.run()
  }
}
