package net.orfjackal.specsy.runner

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit._
import net.orfjackal.specsy.Specsy

class SuiteRunnerTest {
  val collector = new ResultCollector
  val runner = new SuiteRunner(collector)

  @Test
  def all_tests_in_the_suite_are_included_in_results() {
    runner.add(classOf[DummySpec1])
    runner.add(classOf[DummySpec2])
    runner.run()

    assertThat(collector.visualizedTree, is(List(
      classOf[DummySpec1].getName,
      "- DummySpec1",
      "  - child A",
      "  - child B",
      classOf[DummySpec2].getName,
      "- DummySpec2",
      "  - child C",
      "  - child D"
      )))
  }
}

private class DummySpec1 extends Specsy {
  "child A" >> {}
  "child B" >> {}
}

private class DummySpec2 extends Specsy {
  "child C" >> {}
  "child D" >> {}
}
