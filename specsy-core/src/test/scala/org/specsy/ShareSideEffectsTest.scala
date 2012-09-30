// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import util.TestHelpers

class ShareSideEffectsTest extends TestHelpers {

  @Test
  def children_are_affected() {
    runSpec(c => {
      c.bootstrap("root", {
        c.shareSideEffects()
        spy.append("root")

        c.spec("child A", {
          spy.append("A")
        })
        c.spec("child A", {
          spy.append("B")
        })
      })
    })

    assertSpyContains("root", "A", "B")
  }

  @Test
  def nested_children_are_affected() {
    runSpec(c => {
      c.bootstrap("root", {
        c.shareSideEffects()
        spy.append("root")

        c.spec("child A", {
          spy.append("A")

          c.spec("child AA", {
            spy.append("AA")
          })
          c.spec("child AB", {
            spy.append("AB")
          })
        })
      })
    })

    assertSpyContains("root", "A", "AA", "AB")
  }

  @Test
  def siblings_are_not_affected() {
    runSpec(c => {
      c.bootstrap("root", {
        spy.append("root")

        c.spec("child A", {
          c.shareSideEffects()
          spy.append("A")
        })
        c.spec("child B", {
          spy.append("B")
        })
      })
    })

    assertSpyContains("root", "A", "root", "B")
  }
}
