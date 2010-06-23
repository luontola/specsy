package net.orfjackal.specsy.internal

object Path {
  def apply(indexes: Int*): Path = Path(IndexedSeq(indexes: _*))
}

case class Path(indexes: IndexedSeq[Int]) {
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

  private def length = indexes.length

  private def lastIndex = indexes.last
}
