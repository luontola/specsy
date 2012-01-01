// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

object ContextDealer {
  private val prepared = new ThreadLocal[Context]

  def prepare(context: Context) {
    prepared.set(context)
  }

  def take(): Context = {
    val context = prepared.get
    assert(context != null, "tried to take the context before it was prepared")
    prepared.set(null)
    context
  }
}
