package net.orfjackal.specsy.internal

class Context(targetPath: Path) {
  private var currentSpec: Spec = null
  private var executed: Path = null
  private var postponed = List[Path]()

  def this() = this (Path())

  def specify(name: String, body: => Unit) {
    enterSpec(name)
    processSpec(body)
    exitSpec()
  }

  private def enterSpec(name: String) {
    if (currentSpec == null) {
      currentSpec = new Spec(name, null, Path(), targetPath)
    } else {
      currentSpec = currentSpec.addChild(name)
    }
  }

  private def processSpec(body: => Unit) {
    if (currentSpec.shouldExecute) {
      executed = currentSpec.currentPath
      try {
        body
      } catch {
        case e => e.printStackTrace
      }
    }
    if (currentSpec.shouldPostpone) {
      postponed = currentSpec.currentPath :: postponed
    }
  }

  private def exitSpec() {
    currentSpec = currentSpec.parent
  }

  def executedPath: Path = {
    assert(executed != null)
    executed
  }

  def postponedPaths: List[Path] = {
    postponed
  }
}
