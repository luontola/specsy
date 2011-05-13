package net.orfjackal.specsy.core

class SpecDeclaration(
        val name: String,
        val parent: SpecDeclaration,
        val path: Path,
        targetPath: Path
        ) {
  private var nextChild = path.firstChild
  private var children = List[SpecDeclaration]()
  private var _deferred = List[() => Unit]()

  def addChild(childName: String): SpecDeclaration = {
    val child = new SpecDeclaration(childName, this, pathOfNextChild(), targetPath)
    children = child :: children
    child
  }

  private def pathOfNextChild() = {
    val path = nextChild
    nextChild = nextChild.nextSibling
    path
  }

  def addDefer(body: => Unit) {
    _deferred = (body _) :: _deferred
  }

  def deferred = _deferred

  def shouldExecute: Boolean = isOnTargetPath || (isUnseen && isFirstChild)

  def shouldPostpone: Boolean = isUnseen && !isFirstChild

  private def isOnTargetPath = path.isOn(targetPath)

  private def isUnseen = path.isBeyond(targetPath)

  private def isFirstChild = path.isFirstChild
}
