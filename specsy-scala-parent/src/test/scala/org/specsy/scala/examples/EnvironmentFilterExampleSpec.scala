// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala.examples

import fi.jumi.api.RunVia
import org.specsy.Specsy
import org.specsy.scala.ScalaSpecsy

@RunVia(classOf[Specsy])
class EnvironmentFilterExampleSpec extends ScalaSpecsy {

  "This test is run every time" >> {
    // Test code...
  }

  "This test is run only under Java 8 and greater" >> worksOnlyOnJava8 {
    // Test code... For example something which uses the new Date and Time API (JSR-310)
    // which should be included in Java 8
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
      Class.forName("javax.time.calendar.LocalDate")
      true
    } catch {
      case e: ClassNotFoundException => false
    }
  }
}
