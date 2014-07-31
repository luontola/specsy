// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala.examples

import org.specsy.scala.ScalaSpecsy

class EnvironmentFilterExampleSpec extends ScalaSpecsy {

  "This test is run every time" >> {
    // Test code...
  }

  "This test is run only under Java 8 and greater" >> worksOnlyOnJava8 {
    // Test code... For example something which uses the new Date and Time API (JSR-310)
    // which was added in Java 8
  }

  // This can also be used at the top level, if many/all tests work only on Java 8.
  // Just surround all tests and variable/field declarations into a closure.
  worksOnlyOnJava8 {

    "This requires Java 8" >> {
      // Test code...
    }
    "This also requires Java 8" >> {
      // Test code...
    }
  }


  private def worksOnlyOnJava8(closure: => Unit) {
    if (isJava8) {
      closure
    }
  }

  private def isJava8: Boolean = {
    try {
      Class.forName("java.time.LocalDate")
      true
    } catch {
      case e: ClassNotFoundException => false
    }
  }
}
