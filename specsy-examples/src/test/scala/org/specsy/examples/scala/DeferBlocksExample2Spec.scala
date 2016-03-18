// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.scala

import org.specsy.scala.ScalaSpecsy
import java.nio.file.{Files, Path, Paths}
import java.util.UUID

class DeferBlocksExample2Spec extends ScalaSpecsy {
  val dir = createWithCleanup(Paths.get("temp-directory-" + UUID.randomUUID()), Files.createDirectory(_))
  val file1 = createWithCleanup(dir.resolve("file 1.txt"), Files.createFile(_))

  "..." >> {
  }

  "..." >> {
    val file2 = createWithCleanup(dir.resolve("file 2.txt"), Files.createFile(_))
  }

  def createWithCleanup(path: Path, create: Path => Any): Path = {
    println("Creating " + path)
    create(path)
    defer {
      println("Deleting " + path)
      Files.delete(path)
    }
    path
  }
}
