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
      execute(body)
    }
    if (current.shouldPostpone) {
      // TODO: store in FIFO order, to make the order more predictable. See http://groups.google.com/group/specs-users/msg/7c1c544bc7dfc1ed
      postponed = current.path :: postponed
    }
  }

  private def execute(body: => Unit): Unit = {
    notifier.fireTestFound(current.path, current.name, (body _).getClass)
    val tn = notifier.fireTestStarted(current.path)

    executeSafely(body _, tn)
    current.deferred.foreach(executeSafely(_, tn))

    tn.fireTestFinished()
  }

  private def executeSafely(body: Function0[Unit], tn: TestNotifier) {
    try {
      body.apply()
    } catch {
      case e => tn.fireFailure(e)
    }
  }

  private def exitSpec() {
    current = current.parent
  }

  def defer(body: => Unit) {
    current.addDefer(body)
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
