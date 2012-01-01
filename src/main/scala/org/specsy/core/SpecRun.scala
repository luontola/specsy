// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import org.specsy.runner.notification._

class SpecRun(spec: Context => Unit, pathToExecute: Path, notifier: SuiteNotifier) extends Runnable {
  def this(spec: Context => Unit, notifier: SuiteNotifier) = this (spec, Path.Root, notifier)

  def run() {
    val c = executePath(spec, pathToExecute)
    for (postponedPath <- c.postponedPaths) {
      notifier.submitTestRun(new SpecRun(spec, postponedPath, notifier))
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
