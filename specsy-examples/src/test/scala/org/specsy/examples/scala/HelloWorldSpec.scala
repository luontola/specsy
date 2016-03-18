// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.scala

import org.specsy.scala.ScalaSpecsy

class HelloWorldSpec extends ScalaSpecsy {

  // top-level spec; add your test code here and/or the child specs

  "name of the spec" >> {
    // first child spec
  }

  "name of the spec" >> {
    // second child spec

    "name of the spec" >> {
      // a nested child spec
    }
  }
}
