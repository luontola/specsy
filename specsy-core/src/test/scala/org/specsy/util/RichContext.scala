// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.util

import org.specsy.core.Context

class RichContext(context: Context) {

  def bootstrap[U](className: String, rootSpec: => U) {
    context.bootstrap(className, new ByNameClosure(rootSpec))
  }

  def specify[U](name: String, body: => U) {
    context.specify(name, new ByNameClosure(body))
  }

  def defer[U](body: => U) {
    context.defer(new ByNameClosure(body))
  }
}
