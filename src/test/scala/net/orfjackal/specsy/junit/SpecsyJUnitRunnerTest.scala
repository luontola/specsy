package net.orfjackal.specsy.junit

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import net.orfjackal.specsy.Specsy
import org.junit.runner._
import org.junit._

class SpecsyJUnitRunnerTest {
  val testClass = classOf[DummySpecWithTwoChildren]
  val runner = new SpecsyJUnitRunner(testClass)

  @Test
  def runs_specs_using_JUnit() {
    val result = new JUnitCore().run(runner)
    assertThat("run count", result.getRunCount, is(4))
  }

  @Test
  def reports_the_names_of_executed_specs() {
    val root = runner.getDescription

    assertTrue(root.isSuite)
    assertThat(root.getClassName, is(testClass.getName))

    val childA = root.getChildren.get(0)
    val childB = root.getChildren.get(1)
    assertTrue(childA.isSuite)
    assertThat(childA.getDisplayName, is("child A"))
    assertTrue(childB.isTest)
    assertThat(childB.getMethodName, is("child B"))
  }

  @Test
  def reports_hierarchies_of_deeply_nested_specs() {
    val root = runner.getDescription

    val childA = root.getChildren.get(0)
    val childAA = childA.getChildren.get(0)
    assertTrue(childAA.isTest)
    assertThat(childAA.getMethodName, is("child AA"))
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
