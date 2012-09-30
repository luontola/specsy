// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.util

import fi.jumi.core.results.SuiteEventDemuxer
import fi.jumi.core.testbench.{StubDriverFinder, TestBench}
import fi.jumi.api.drivers.{SuiteNotifier, Driver}
import java.util.concurrent.Executor
import org.specsy.core.SpecRun
import collection.mutable
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._

trait TestHelpers {

  val spy = mutable.Buffer[String]()

  def assertSpyContains(expected: String*) {
    assertThat(spy, is(Seq(expected: _*)))
  }

  def runSpec(spec: ScalaContext => Unit): SuiteEventDemuxer = {
    val bench = new TestBench()
    bench.setDriverFinder(new StubDriverFinder(new Driver {
      def findTests(testClass: Class[_], notifier: SuiteNotifier, executor: Executor) {
        executor.execute(new SpecRun(new FunctionSpec(spec), notifier, executor))
      }
    }))
    bench.run(getClass)
  }
}
