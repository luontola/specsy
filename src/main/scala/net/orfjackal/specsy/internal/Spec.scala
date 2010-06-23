package net.orfjackal.specsy.internal

class Spec(
        val name: String,
        val parent: Spec,
        val currentPath: Path,
        targetPath: Path
        ) {
  private var nextChild = currentPath.firstChild
  private var children = List[Spec]()

  def addChild(childName: String): Spec = {
    val child = new Spec(childName, this, pathOfNextChild(), targetPath)
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
