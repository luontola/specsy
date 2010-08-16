package net.orfjackal.specsy.examples

import org.junit.runner.RunWith
import net.orfjackal.specsy._
import java.io.File

@RunWith(classOf[Specsy])
class DeferBlocksExample2Spec extends Spec {
  val dir = createWithCleanup(new File("a directory"), _.mkdir(), _.delete())
  val file1 = createWithCleanup(new File(dir, "file 1.txt"), _.createNewFile(), _.delete())

  "..." >> {
  }

  "..." >> {
    val file2 = createWithCleanup(new File(dir, "file 2.txt"), _.createNewFile(), _.delete())
  }

  def createWithCleanup(file: File, create: File => Boolean, delete: File => Boolean): File = {
    assert(create(file), "failed to create: " + file)
    defer {
      assert(delete(file), "failed to delete: " + file)
    }
    file
  }
}
