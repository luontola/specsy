package net.orfjackal.specsy

object Path {
  def apply(indexes: Int*): Path = {
    new Path(IndexedSeq(indexes: _*))
  }
}

case class Path(indexes: IndexedSeq[Int]) {
  def childAtIndex(index: Int): Path = {
    new Path(indexes :+ index)
  }

  def isOn(target: Path): Boolean = {
    indexes == target.indexes.take(indexes.length)
  }

  def isBeyond(target: Path): Boolean = {
    target.isOn(this) && indexes.length > target.indexes.length
  }

  def lastIndex: Int = {
    indexes.last
  }
}
