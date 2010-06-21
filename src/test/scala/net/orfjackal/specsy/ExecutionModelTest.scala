package net.orfjackal.specsy

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import collection.mutable.{Buffer, ArrayBuffer}

class ExecutionModelTest {
  val c = new Context
  val spy = Buffer[String]()

  @Test
  def there_is_one_root_spec() {
    c.specify("root", {
      spy.append("root")
    })

    assertThat(spy, is(Buffer("root")))
  }

  @Test
  def the_root_spec_can_contain_multiple_nested_child_specs() {
    c.specify("root", {
      spy.append("root")

      c.specify("child A", {
        spy.append("A")

        c.specify("child AA", {
          spy.append("AA")
        })
      })
    })

    assertThat(spy, is(Buffer("root", "A", "AA")))
  }
}
