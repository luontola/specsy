// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala.examples

import fi.jumi.api.RunVia
import org.specsy.Specsy
import org.specsy.scala.ScalaSpecsy
import java.util.UUID
import java.nio.file.{Files, Paths}

@RunVia(classOf[Specsy])
class DeferBlocksExampleSpec extends ScalaSpecsy {
  val dir = Paths.get("temp-directory-" + UUID.randomUUID())
  Files.createDirectory(dir)
  defer {
    Files.delete(dir)
  }

  val file1 = dir.resolve("file 1.txt")
  Files.createFile(file1)
  defer {
    Files.delete(file1)
  }

  "..." >> {
    // do something with the files
  }

  "..." >> {
    // child specs can also use defer blocks
    val file2 = dir.resolve("file 2.txt")
    Files.createFile(file2)
    defer {
      Files.delete(file2)
    }

    // 'file2' will be deleted when this child spec exits
  }

  // will delete first 'file1' and second 'dir'
  // (or if creating 'file1' failed, then will delete only 'dir')
}
