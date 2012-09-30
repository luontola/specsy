// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.util

import org.specsy.core.Context

class ScalaContext(context: Context) {

  def bootstrap(className: String, rootSpec: => Unit) {
    context.bootstrap(className, new ByNameClosure(rootSpec))
  }

  def spec(name: String, spec: => Unit) {
    context.spec(name, new ByNameClosure(spec))
  }

  def defer(block: => Unit) {
    context.defer(new ByNameClosure(block))
  }

  def shareSideEffects() {
    context.shareSideEffects()
  }
}
