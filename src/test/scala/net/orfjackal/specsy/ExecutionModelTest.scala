package net.orfjackal.specsy

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import collection.mutable.{Buffer}
import net.orfjackal.specsy.internal._

class ExecutionModelTest {
  val runner = new SpecRunner
  val spy = Buffer[String]()

  @Test
  def there_is_one_root_spec() {
    runner.run(c => {
      c.run("root", {
        spy.append("root")
      })
    })

    assertThat(spy, is(Buffer("root")))
  }

  @Test
  def the_root_spec_can_contain_multiple_nested_child_specs() {
    runner.run(c => {
      c.run("root", {
        spy.append("root")

        c.specify("child A", {
          spy.append("A")

          c.specify("child AA", {
            spy.append("AA")
          })
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "AA")))
  }

  @Test
  def the_specs_are_executed_one_branch_at_a_time_until_all_are_executed() {
    runner.run(c => {
      c.run("root", {
        spy.append("root")

        c.specify("child A", {
          spy.append("A")
        })
        c.specify("child B", {
          spy.append("B")
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "root", "B")))
  }

  @Test
  def variables_declared_inside_specs_are_isolated_from_the_side_effects_of_sibling_specs() {
    runner.run(c => {
      c.run("root", {
        var i = 0

        c.specify("child A", {
          i += 1
          spy.append("A" + i)
        })
        c.specify("child B", {
          i += 1
          spy.append("B" + i)
        })
      })
    })

    assertThat(spy, is(Buffer("A1", "B1")))
  }
}
