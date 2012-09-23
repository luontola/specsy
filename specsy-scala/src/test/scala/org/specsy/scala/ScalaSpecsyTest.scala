// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala

import org.specsy.{GlobalSpy, SpecsyContract, Specsy}
import fi.jumi.api.RunVia

class ScalaSpecsyTest extends SpecsyContract {
  val testClass = classOf[DummyScalaSpecsyTest]
}

@RunVia(classOf[Specsy])
class DummyScalaSpecsyTest extends ScalaSpecsy {
  var i = 0

  "name of a spec" >> {
    GlobalSpy.add("spec executed")
  }

  "defer blocks" >> {
    defer {
      GlobalSpy.add("defer 1")
    }
    defer {
      GlobalSpy.add("defer 2")
    }
  }

  "isolated" >> {
    "mutation 1" >> {
      i += 1
    }
    "mutation 2" >> {
      i += 1
    }
    GlobalSpy.add("isolated: " + i)
  }

  "non-isolated" >> {
    shareSideEffects()
    "mutation 1" >> {
      i += 1
    }
    "mutation 2" >> {
      i += 1
    }
    GlobalSpy.add("non-isolated: " + i)
  }
}
