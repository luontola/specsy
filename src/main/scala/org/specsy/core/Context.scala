// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import Context._
import org.specsy.runner.notification._
import scala.collection.mutable.Buffer

object Context {
  abstract sealed class Status
  case object NotStarted extends Status
  case object Running extends Status
  case object Finished extends Status
}

class Context(targetPath: Path, notifier: SuiteNotifier) {
  private var status: Status = NotStarted
  private var current: SpecDeclaration = null
  private val postponed = Buffer[Path]()

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
      postponed += current.path
    }
  }

  private def execute(body: => Unit) {
    notifier.fireTestFound(current.path, current.name, (body _).getClass)
    val tn = notifier.fireTestStarted(current.path)

    executeSafely(body _, tn)
    current.deferred.foreach(executeSafely(_, tn))

    tn.fireTestFinished()
  }

  private def executeSafely(body: () => Unit, tn: TestNotifier) {
    try {
      body.apply()
    } catch {
      case e => tn.fireFailure(e)
    }
  }

  private def exitSpec() {
    current = current.parent
  }

  def shareSideEffects() {
    current.shareSideEffects()
  }

  def defer(body: => Unit) {
    current.addDefer(body)
  }

  def postponedPaths: List[Path] = {
    assertStatusIs(Finished)
    postponed.toList
  }

  private def changeStatus(from: Status, to: Status) {
    assertStatusIs(from)
    status = to
  }

  private def assertStatusIs(expected: Status) {
    assert(status == expected, "expected status " + expected + ", but was " + status)
  }
}
