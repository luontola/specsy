package net.orfjackal.specsy.examples

import org.junit.runner.RunWith
import net.orfjackal.specsy._
import org.junit.Assert._
import org.hamcrest.CoreMatchers._

@RunWith(classOf[Specsy])
class StackSpec extends Spec {
  val stack = new scala.collection.mutable.Stack[String]

  "An empty stack" >> {

    "is empty" >> {
      assertTrue(stack.isEmpty)
    }
    "After a push, the stack is no longer empty" >> {
      stack.push("a push")
      assertFalse(stack.isEmpty)
    }
  }

  "When objects have been pushed onto a stack" >> {
    stack.push("pushed first")
    stack.push("pushed last")

    "the object pushed last is popped first" >> {
      val poppedFirst = stack.pop()
      assertThat(poppedFirst, is("pushed last"))
    }
    "the object pushed first is popped last" >> {
      stack.pop()
      val poppedLast = stack.pop()
      assertThat(poppedLast, is("pushed first"))
    }
    "After popping all objects, the stack is empty" >> {
      stack.pop()
      stack.pop()
      assertTrue(stack.isEmpty)
    }
  }
}
