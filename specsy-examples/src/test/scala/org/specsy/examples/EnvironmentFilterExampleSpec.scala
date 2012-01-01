// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples

import org.junit.runner.RunWith
import org.specsy.scala._
import java.net.URLClassLoader

@RunWith(classOf[Specsy])
class EnvironmentFilterExampleSpec extends Spec {

  "This test is run every time" >> {
    // Test code...
  }

  "This test is run only under Java 7 and greater" >> worksOnlyOnJava7 {
    // Test code... For example something which does custom class loading
    // and requires the URLClassLoader.close() method which was added in Java 7.
  }

  // This can also be used at the top level, if many/all tests work only on Java 7.
  // Just surround all tests and variable/field declarations into a closure.
  worksOnlyOnJava7 {

    "This requires Java 7" >> {
      // Test code...
    }
    "This also requires Java 7" >> {
      // Test code...
    }
  }


  private def worksOnlyOnJava7(closure: => Unit) {
    if (isJava7) {
      closure
    }
  }

  private def isJava7: Boolean = {
    try {
      classOf[URLClassLoader].getMethod("close")
      true
    } catch {
      case e: NoSuchMethodException => false
    }
  }
}
