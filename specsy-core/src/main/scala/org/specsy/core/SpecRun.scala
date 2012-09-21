// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import fi.jumi.api.drivers
import java.util.concurrent.Executor

class SpecRun(spec: Context => Unit,
              pathToExecute: Path,
              notifier: drivers.SuiteNotifier,
              executor: Executor) extends Runnable {

  def this(spec: Context => Unit,
           notifier: drivers.SuiteNotifier,
           executor: Executor) =
    this(spec, Path.Root, notifier, executor)

  def run() {
    val c = executePath(spec, pathToExecute)
    for (postponedPath <- c.postponedPaths) {
      executor.execute(new SpecRun(spec, postponedPath, notifier, executor))
    }
  }

  private def executePath(spec: Context => Unit, path: Path): Context = {
    val c = new Context(path, notifier)
    executeSafely(c, spec)
    c
  }

  private def executeSafely(c: Context, spec: Context => Unit) {
    try {
      spec(c)
    } catch {
      case e =>
        e.printStackTrace()
        throw new RuntimeException("internal error", e)
    }
  }
}
