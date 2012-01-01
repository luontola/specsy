// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.runner.notification

trait TestNotifier {
  def fireFailure(cause: Throwable)

  def fireTestFinished()
}
