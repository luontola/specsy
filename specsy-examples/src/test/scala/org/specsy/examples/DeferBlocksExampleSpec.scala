// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples

import org.junit.runner.RunWith
import org.specsy.scala._
import java.io.File
import java.util.UUID

@RunWith(classOf[Specsy])
class DeferBlocksExampleSpec extends Spec {
  val dir = new File("temp-directory-" + UUID.randomUUID())
  assert(dir.mkdir(), "failed to create: " + dir)
  defer {
    assert(dir.delete(), "failed to delete: " + dir)
  }

  val file1 = new File(dir, "file 1.txt")
  assert(file1.createNewFile(), "failed to create: " + file1)
  defer {
    assert(file1.delete(), "failed to delete:" + file1)
  }

  "..." >> {
    // do something with the files
  }

  "..." >> {
    // child specs can also use defer blocks
    val file2 = new File(dir, "file 2.txt")
    assert(file2.createNewFile(), "failed to create: " + file2)
    defer {
      assert(file2.delete(), "failed to delete:" + file2)
    }

    // 'file2' will be deleted when this child spec exits
  }

  // will delete first 'file1' and second 'dir'
  // (or if creating 'file1' failed, then will delete only 'dir')
}
