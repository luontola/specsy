// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.util

import org.specsy.core.Context

class RichContext(context: Context) {

  def bootstrap[U](className: String, rootSpec: => U) {
    context.bootstrap(className, new ByNameClosure(rootSpec))
  }

  def spec[U](name: String, spec: => U) {
    context.spec(name, new ByNameClosure(spec))
  }

  def defer[U](block: => U) {
    context.defer(new ByNameClosure(block))
  }
}
