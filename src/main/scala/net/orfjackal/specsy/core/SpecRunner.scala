package net.orfjackal.specsy.core

import net.orfjackal.specsy.runner.notification._

class SpecRunner(spec: Context => Unit, pathToExecute: Path, notifier: SuiteNotifier) extends Runnable {
  def this(spec: Context => Unit, notifier: SuiteNotifier) = this (spec, Path.Root, notifier)

  def run() {
    val c = executePath(spec, pathToExecute)
    for (postponedPath <- c.postponedPaths) {
      notifier.submitTestRun(new SpecRunner(spec, postponedPath, notifier))
    }
  }

  private def executePath(spec: Context => Unit, path: Path): Context = {
    val c = new Context(path, notifier)
    executeSafely(c, spec)
    c
  }

  private def executeSafely(c: Context, spec: Context => Unit): Unit = {
    try {
      spec(c)
    } catch {
      case e =>
        e.printStackTrace
        throw new RuntimeException("internal error", e)
    }
  }
}
