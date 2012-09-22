// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

class RichContext(context: Context) {

  def bootstrap(className: String, rootSpec: => Unit) {
    context.bootstrap(className, new ClosureAdapter(rootSpec))
  }

  def specify(name: String, body: => Unit) {
    context.specify(name, new ClosureAdapter(body))
  }

  def defer(body: => Unit) {
    context.defer(new ClosureAdapter(body))
  }
}
