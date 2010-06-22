package net.orfjackal.specsy.internal

object Path {
  def apply(indexes: Int*): Path = {
    new Path(IndexedSeq(indexes: _*))
  }
}

case class Path(indexes: IndexedSeq[Int]) {
  def length: Int = indexes.length

  def lastIndex: Int = {
    indexes.last
  }

  def childAtIndex(index: Int): Path = {
    new Path(indexes :+ index)
  }

  def isOn(that: Path): Boolean = {
    this == that.prefixOfLength(this.length)
  }

  private def prefixOfLength(len: Int): Path = {
    Path(this.indexes.take(len))
  }

  def isBeyond(that: Path): Boolean = {
    that.isOn(this) && this.length > that.length
  }
}
