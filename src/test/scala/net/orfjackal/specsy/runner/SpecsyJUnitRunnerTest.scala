package net.orfjackal.specsy.runner

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.Specsy
import org.junit.runner._

class SpecsyJUnitRunnerTest {
  @Test
  def runs_specs_using_JUnit() {
    val result = JUnitCore.runClasses(classOf[DummySpecWithTwoChildren])
    assertThat(result.getRunCount, is(2))
  }
}

@RunWith(classOf[SpecsyJUnitRunner])
class DummySpecWithTwoChildren extends Specsy {
  "child A" >> {
  }
  "child B" >> {
  }
}
