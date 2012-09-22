// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

class ClosureAdapter(body: => Unit) extends Closure {
  def run() {
    body
  }
}
