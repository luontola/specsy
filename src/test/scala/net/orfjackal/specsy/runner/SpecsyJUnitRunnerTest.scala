package net.orfjackal.specsy.runner

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.Specsy
import org.junit.runner._
import org.junit._

class SpecsyJUnitRunnerTest {
  val runner = new SpecsyJUnitRunner(classOf[DummySpecWithTwoChildren])

  @Test
  def runs_specs_using_JUnit() {
    val result = new JUnitCore().run(runner)
    assertThat(result.getRunCount, is(2))
  }

  @Ignore("reporting hierarchies not implemented")
  @Test
  def reports_the_names_of_executed_specs() {
    val desc = runner.getDescription
    assertThat(desc.getClassName, is(classOf[DummySpecWithTwoChildren].getName))
    assertThat(desc.getChildren.get(0).getMethodName, is("child A"))
    assertThat(desc.getChildren.get(1).getMethodName, is("child B"))
  }

  @Ignore("reporting hierarchies not implemented")
  @Test
  def reports_hierarchies_of_deeply_nested_specs() {
    val desc = runner.getDescription
    assertThat(desc.getChildren.get(0).getChildren.get(0).getMethodName, is("child AA"))
  }
}

@RunWith(classOf[SpecsyJUnitRunner])
class DummySpecWithTwoChildren extends Specsy {
  "child A" >> {
    "child AA" >> {
    }
  }
  "child B" >> {
  }
}
