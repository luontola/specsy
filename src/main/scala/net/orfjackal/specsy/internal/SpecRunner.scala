package net.orfjackal.specsy.internal

import scala.collection.mutable.Buffer

class SpecRunner {
  def run(spec: Context => Unit): SpecResult = {
    var postponed = Buffer[Path]()
    postponed.append(Path())

    var runCount = 0
    while (postponed.length > 0) {
      val pathToExecute = postponed.remove(0)
      runCount += 1

      val c = new Context(pathToExecute)
      spec(c)

      postponed.appendAll(c.postponedPaths)
    }

    SpecResult(runCount)
  }
}
