package net.orfjackal.specsy.core

object Path {
  def apply(indexes: Int*): Path = Path(IndexedSeq(indexes: _*))

  val Root: Path = Path()
}

case class Path(indexes: IndexedSeq[Int]) extends Ordered[Path] {
  for (i <- indexes) {
    require(i >= 0, "all indexes must be >= 0, but was " + indexes)
  }

  def isRoot: Boolean = length == 0

  def parent: Path = Path(indexes.dropRight(1))

  def isFirstChild: Boolean = lastIndex == 0

  def firstChild: Path = childAtIndex(0)

  def nextSibling: Path = parent.childAtIndex(lastIndex + 1)

  private def childAtIndex(index: Int): Path = Path(indexes :+ index)

  def isOn(that: Path): Boolean =
    this == that.prefixOfLength(this.length)

  def isBeyond(that: Path): Boolean =
    that.isOn(this) && this.length > that.length

  private def prefixOfLength(len: Int) = Path(indexes.take(len))

  def compare(that: Path): Int = {
    var i = 0
    while (i < this.length && i < that.length) {
      val thisIndex = this.indexAt(i)
      val thatIndex = that.indexAt(i)
      if (thisIndex != thatIndex) {
        return thisIndex - thatIndex
      }
      i += 1
    }
    this.length - that.length
  }

  def length = indexes.length

  def indexAt(i: Int): Int = indexes(i)

  private def lastIndex = indexes.last
}
