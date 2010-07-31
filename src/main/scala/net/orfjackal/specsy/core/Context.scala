package net.orfjackal.specsy.core

import Context._
import net.orfjackal.specsy.runner.notification._

object Context {
  abstract sealed class Status
  case object NotStarted extends Status
  case object Running extends Status
  case object Finished extends Status
}

class Context(targetPath: Path, notifier: SuiteNotifier) {
  private var status: Status = NotStarted
  private var current: SpecDeclaration = null
  private var postponed = List[Path]()

  def bootstrap(className: String, rootSpec: => Unit) {
    changeStatus(NotStarted, Running)

    enterRootSpec(className)
    processSpec(rootSpec)
    exitSpec()

    changeStatus(Running, Finished)
  }

  private def enterRootSpec(name: String) {
    current = new SpecDeclaration(name, null, Path.Root, targetPath)
  }

  def specify(name: String, body: => Unit) {
    assertStatusIs(Running)

    enterSpec(name)
    processSpec(body)
    exitSpec()
  }

  private def enterSpec(name: String) {
    current = current.addChild(name)
  }

  private def processSpec(body: => Unit) {
    if (current.shouldExecute) {
      executeSafely(body)
    }
    if (current.shouldPostpone) {
      postponed = current.path :: postponed
    }
  }

  private def executeSafely(body: => Unit) {
    notifier.fireTestFound(current.path, current.name, (body _).getClass)
    val tn = notifier.fireTestStarted(current.path)
    try {
      body
    } catch {
      case e => tn.fireFailure(e)
    }
    tn.fireTestFinished()
  }

  private def exitSpec() {
    current = current.parent
  }

  def postponedPaths: List[Path] = {
    assertStatusIs(Finished)
    postponed
  }

  private def changeStatus(from: Status, to: Status) {
    assertStatusIs(from)
    status = to
  }

  private def assertStatusIs(expected: Status) {
    assert(status == expected, "expected status " + expected + ", but was " + status)
  }
}
