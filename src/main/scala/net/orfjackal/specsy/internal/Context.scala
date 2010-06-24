package net.orfjackal.specsy.internal

class Context(targetPath: Path) {
  private var status: Context.Status = Context.NotStarted
  private var current: SpecRun = null
  private var executed: SpecRun = null // TODO: make 'executed' a list?
  private var postponed = List[Path]()
  private var _failures = List[(SpecRun, Throwable)]()

  def this() = this (Path.Root)

  def run(className: String, rootSpec: => Unit) {
    changeStatus(Context.NotStarted, Context.Running)

    enterRootSpec(className)
    processSpec(rootSpec)
    exitSpec()

    changeStatus(Context.Running, Context.Finished)
  }

  private def enterRootSpec(name: String) {
    current = new SpecRun(name, null, Path.Root, targetPath)
  }

  def specify(name: String, body: => Unit) {
    assertStatusIs(Context.Running)

    enterSpec(name)
    processSpec(body)
    exitSpec()
  }

  private def enterSpec(name: String) {
    current = current.addChild(name)
  }

  private def processSpec(body: => Unit) {
    if (current.shouldExecute) {
      // TODO: there is no test that this assignment must be first (otherwise the path will be root's path, and IDEA's test runner will get confused)
      executed = current
      executeSafely(body)
    }
    if (current.shouldPostpone) {
      postponed = current.currentPath :: postponed
    }
  }

  private def executeSafely(body: => Unit) {
    try {
      body
    } catch {
      case e => _failures ::= (current, e)
    }
  }

  private def exitSpec() {
    current = current.parent
  }

  def executedSpec: SpecRun = {
    assertStatusIs(Context.Finished)
    executed
  }

  def executedPath: Path = {
    assertStatusIs(Context.Finished)
    executed.currentPath
  }

  def postponedPaths: List[Path] = {
    assertStatusIs(Context.Finished)
    postponed
  }

  def failures: List[(SpecRun, Throwable)] = {
    assertStatusIs(Context.Finished)
    _failures
  }

  private def changeStatus(from: Context.Status, to: Context.Status) {
    assertStatusIs(from)
    status = to
  }

  private def assertStatusIs(expected: Context.Status) {
    assert(status == expected, "Should have been " + expected + ", but was " + status)
  }
}

object Context {
  abstract class Status
  case object NotStarted extends Status
  case object Running extends Status
  case object Finished extends Status
}
