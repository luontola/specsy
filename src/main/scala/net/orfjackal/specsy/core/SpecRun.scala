package net.orfjackal.specsy.core

class SpecRun(
        val name: String,
        val parent: SpecRun,
        val path: Path,
        targetPath: Path
        ) {
  private var nextChild = path.firstChild
  private var children = List[SpecRun]()

  def addChild(childName: String): SpecRun = {
    val child = new SpecRun(childName, this, pathOfNextChild(), targetPath)
    children = child :: children
    child
  }

  private def pathOfNextChild() = {
    val path = nextChild
    nextChild = nextChild.nextSibling
    path
  }

  def shouldExecute: Boolean = isOnTargetPath || (isUnseen && isFirstChild)

  def shouldPostpone: Boolean = isUnseen && !isFirstChild

  private def isOnTargetPath = path.isOn(targetPath)

  private def isUnseen = path.isBeyond(targetPath)

  private def isFirstChild = path.isFirstChild
}
