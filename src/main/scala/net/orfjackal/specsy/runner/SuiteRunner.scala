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
