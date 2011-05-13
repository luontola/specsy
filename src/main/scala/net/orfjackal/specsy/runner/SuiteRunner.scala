// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.runner

import java.util.concurrent.LinkedBlockingQueue

class SuiteRunner {
  private val runQueue = new LinkedBlockingQueue[Runnable]

  def submitTestRun(testRun: Runnable) {
    runQueue.put(testRun)
  }

  def await() {
    while (!runQueue.isEmpty) {
      val testRun = runQueue.take()
      testRun.run()
    }
  }
}
