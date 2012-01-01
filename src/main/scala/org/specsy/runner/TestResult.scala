// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.runner

import org.specsy.core.Path

trait TestResult {
  def path: Path

  def name: String

  def location: Object

  def failures: List[Throwable]

  def output: String
}
