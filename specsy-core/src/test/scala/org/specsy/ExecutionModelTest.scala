// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import util.TestHelpers

class ExecutionModelTest extends TestHelpers {

  @Test
  def there_is_one_root_spec() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")
      })
    })

    assertSpyContains("root")
  }

  @Test
  def the_root_spec_can_contain_multiple_nested_child_specs() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")

        c.spec("child A", {
          spy.append("A")

          c.spec("child AA", {
            spy.append("AA")
          })
        })
      })
    })

    assertSpyContains("root", "A", "AA")
  }

  @Test
  def the_specs_are_executed_one_branch_at_a_time_until_all_are_executed() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")

        c.spec("child A", {
          spy.append("A")
        })
        c.spec("child B", {
          spy.append("B")
        })
      })
    })

    assertSpyContains("root", "A", "root", "B")
  }

  @Test
  def in_single_threaded_mode_the_child_specs_are_executed_in_declaration_order() {
    runSpec(c => {
      c.bootstrap("root", {
        c.spec("child A", {
          spy.append("A")
        })
        c.spec("child B", {
          spy.append("B")
        })
        c.spec("child C", {
          spy.append("C")
        })
        c.spec("child D", {
          spy.append("D")
        })
      })
    })

    assertSpyContains("A", "B", "C", "D")
  }

  @Test
  def variables_declared_inside_specs_are_isolated_from_the_side_effects_of_sibling_specs() {
    runSpec(c => {
      c.bootstrap("root", {
        var i = 0

        c.spec("child A", {
          i += 1
          spy.append("A" + i)
        })
        c.spec("child B", {
          i += 1
          spy.append("B" + i)
        })
      })
    })

    assertSpyContains("A1", "B1")
  }

  @Test
  def child_specs_are_executed_immediately_where_they_are_declared() {
    runSpec(c => {
      c.bootstrap("root", {
        var i = 0

        c.spec("child A", {
          spy.append("A" + i)
        })
        i += 1

        c.spec("child B", {
          spy.append("B" + i)
        })
      })
    })

    assertSpyContains("A0", "B1")
  }
}
