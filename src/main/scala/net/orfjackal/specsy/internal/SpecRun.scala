package net.orfjackal.specsy.internal

class SpecRun(
        val name: String,
        val parent: SpecRun,
        val currentPath: Path,
        targetPath: Path
        ) {
  private var nextChild = currentPath.firstChild
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

  private def isOnTargetPath = currentPath.isOn(targetPath)

  private def isUnseen = currentPath.isBeyond(targetPath)

  private def isFirstChild = currentPath.isFirstChild
}
