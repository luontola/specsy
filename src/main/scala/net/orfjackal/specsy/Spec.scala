package net.orfjackal.specsy

class Spec(
        val name: String,
        val parent: Spec,
        val currentPath: Path,
        targetPath: Path
        ) {
  private var children = List[Spec]()

  def addChild(childName: String): Spec = {
    val child = new Spec(childName, this, pathOfNextChild, targetPath)
    children = child :: children
    child
  }

  private def pathOfNextChild: Path = {
    val nextChildIndex = children.length
    currentPath.childAtIndex(nextChildIndex)
  }

  def shouldExecute: Boolean = isOnTargetPath || (isUnseen && isFirstChild)

  def shouldPostpone: Boolean = isUnseen && !isFirstChild

  private def isOnTargetPath = currentPath.isOn(targetPath)

  private def isUnseen = currentPath.isBeyond(targetPath)

  private def isFirstChild = currentPath.lastIndex == 0
}

