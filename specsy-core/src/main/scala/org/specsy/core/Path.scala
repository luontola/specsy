// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import fi.jumi.api.drivers.TestId

object Path {
  def apply(indexes: Int*): Path = Path(TestId.of(indexes: _*))

  val Root: Path = Path()
}

case class Path(id: TestId) extends Ordered[Path] {

  def isRoot: Boolean = id.isRoot

  def parent: Path =
    if (id.isRoot) {
      this // XXX: fix usages so that they don't try to get root's parent
    } else {
      Path(id.getParent)
    }

  def isFirstChild: Boolean = id.isFirstChild

  def firstChild: Path = Path(id.getFirstChild)

  def nextSibling: Path = Path(id.getNextSibling)

  def isOn(that: Path): Boolean =
    this.id == that.id || this.id.isAncestorOf(that.id)

  def isBeyond(that: Path): Boolean = this.id.isDescendantOf(that.id)

  def compare(that: Path): Int = {
    this.id.compareTo(that.id)
  }
}
