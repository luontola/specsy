package net.orfjackal.specsy.internal

class Context(targetPath: Path) {
  private var currentSpec: Spec = null
  private var executed: Path = null
  private var postponed = List[Path]()
  private var _failures = List[(Spec, Throwable)]()

  def this() = this (Path())

  def failures: List[(Spec, Throwable)] = _failures

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
      executeSafely(body)
      executed = currentSpec.currentPath
    }
    if (currentSpec.shouldPostpone) {
      postponed = currentSpec.currentPath :: postponed
    }
  }

  private def executeSafely(body: => Unit) {
    try {
      body
    } catch {
      case e => _failures ::= (currentSpec, e)
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
