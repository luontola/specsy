// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.groovy

import fi.jumi.api.RunVia
import org.specsy.GlobalSpy
import org.specsy.Specsy

@RunVia(Specsy)
class GroovySpecsyExample extends GroovySpecsy {

  def counter = 0

  @Override
  void run() {

    // Conventional syntax
    spec("name of a spec") {
      GlobalSpy.add("spec executed")
    }

    // Shorthand syntax, using some Groovy sugar
    spec "defer blocks", {
      defer {
        GlobalSpy.add("defer 1")
      }
      defer {
        GlobalSpy.add("defer 2")
      }
    }

    spec "isolated", {
      spec "mutation 1", {
        counter += 1
      }
      spec "mutation 2", {
        counter += 1
      }
      GlobalSpy.add("isolated: " + counter)
    }

    spec "non-isolated", {
      shareSideEffects()
      spec "mutation 1", {
        counter += 1
      }
      spec "mutation 2", {
        counter += 1
      }
      GlobalSpy.add("non-isolated: " + counter)
    }
  }
}
