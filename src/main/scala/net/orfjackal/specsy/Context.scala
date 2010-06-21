package net.orfjackal.specsy

class Context(targetPath: Path) {
  private var currentSpec: Spec = null
  private var executed: Path = null
  private var postponed = List[Path]()

  def this() = this (Path())

  def specify(name: String, body: => Any) {
    if (currentSpec == null) {
      currentSpec = new Spec(name, null, Path(), targetPath)
    } else {
      currentSpec = currentSpec.addChild(name)
    }

    if (currentSpec.shouldExecute) {
      executed = currentSpec.currentPath
      body
    }
    if (currentSpec.shouldPostpone) {
      postponed = currentSpec.currentPath :: postponed
    }

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
