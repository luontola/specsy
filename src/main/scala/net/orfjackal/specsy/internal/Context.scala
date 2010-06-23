package net.orfjackal.specsy.internal

class Context(targetPath: Path) {
  private var currentSpec: SpecRun = null
  private var executed: SpecRun = null // TODO: make 'executed' a list?
  private var postponed = List[Path]()
  private var _failures = List[(SpecRun, Throwable)]()

  def this() = this (Path())

  def failures: List[(SpecRun, Throwable)] = _failures

  def specify(name: String, body: => Unit) {
    enterSpec(name)
    processSpec(body)
    exitSpec()
  }

  private def enterSpec(name: String) {
    if (currentSpec == null) {
      currentSpec = new SpecRun(name, null, Path(), targetPath)
    } else {
      currentSpec = currentSpec.addChild(name)
    }
  }

  private def processSpec(body: => Unit) {
    if (currentSpec.shouldExecute) {
      // TODO: there is no test that this assignment must be first (otherwise the path will be root's path, and IDEA's test runner will get confused)
      executed = currentSpec
      executeSafely(body)
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

  def executedSpec: SpecRun = {
    assert(executed != null)
    executed
  }

  def executedPath: Path = {
    assert(executed != null)
    executed.currentPath
  }

  def postponedPaths: List[Path] = {
    postponed
  }
}
