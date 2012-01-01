// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.runner.notification

import org.specsy.core.Path

trait SuiteNotifier {
  def fireTestFound(path: Path, name: String, location: Object)

  def submitTestRun(testRun: Runnable)

  def fireTestStarted(path: Path): TestNotifier
}
