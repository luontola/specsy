package net.orfjackal.specsy.examples

import org.junit.runner.RunWith
import net.orfjackal.specsy._
import java.io.File

@RunWith(classOf[Specsy])
class DeferBlocksExampleSpec extends Spec {
  val dir = new File("a directory")
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

    // file2 will be deleted when this child spec exits
  }
}
