// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

class SpecDeclaration(val name: String,
                      val parent: SpecDeclaration,
                      val path: Path,
                      targetPath: Path) {
  private var nextChild = path.firstChild
  private var children = List[SpecDeclaration]()
  private var _shareSideEffects = false
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

  def shareSideEffects() {
    _shareSideEffects = true
  }

  private def isShareSideEffects: Boolean = {
    _shareSideEffects || parent != null && parent.isShareSideEffects
  }

  def addDefer(body: => Unit) {
    _deferred = (body _) :: _deferred
  }

  def deferred = _deferred

  def shouldExecute: Boolean = isOnTargetPath || (isUnseen && isFirstChild) || isShareSideEffects

  def shouldPostpone: Boolean = isUnseen && !isFirstChild && !isShareSideEffects

  private def isOnTargetPath = path.isOn(targetPath)

  private def isUnseen = path.isBeyond(targetPath)

  private def isFirstChild = path.isFirstChild
}
