// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

import Context._
import scala.collection.mutable.Buffer
import fi.jumi.api.drivers.{TestNotifier, SuiteNotifier}

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
    current = new SpecDeclaration(name, null, Path.ROOT, targetPath)
  }

  def specify(name: String, body: => Unit) {
    assertStatusIs(Running)

    enterSpec(name)
    processSpec(body)
    exitSpec()
  }

  private def enterSpec(name: String) {
    current = current.nextChild(name)
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
    notifier.fireTestFound(current.path.toTestId, current.name)
    val tn = notifier.fireTestStarted(current.path.toTestId)

    executeSafely(new Closure {
      def run() {
        body
      }
    }, tn)
    import scala.collection.JavaConversions._
    current.deferred.foreach(executeSafely(_, tn))

    tn.fireTestFinished()
  }

  private def executeSafely(body: Closure, tn: TestNotifier) {
    try {
      body.run()
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
    current.addDefer(new Closure {
      def run() {
        body
      }
    })
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
