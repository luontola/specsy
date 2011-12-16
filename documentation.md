---
title: Documentation
layout: wikistyle
---

Documentation
=============

- [Quick Start](#quick_start)
- [Naming Tests](#naming_tests)
- [Assertions](#assertions)
- [Isolated Execution Model](#isolated_execution_model)
- [Non-Isolated Execution Model](#nonisolated_execution_model)
- [“Before” and “After” Blocks](#before_and_after_blocks)
- [Parameterized Tests](#parameterized_tests)
- [Executing Tests Only in Some Environments](#executing_tests_only_in_some_environments)
- [“Pending Until Fixed”](#pending_until_fixed)


Quick Start
-----------

After you have [configured your dependencies](download.html), you can create a Specsy spec by extending the [Spec] trait. Annotate the class with `@RunWith` to execute it with JUnit. The following shows the structure of a spec:

<pre class="brush: scala">
import org.junit.runner.RunWith
import net.orfjackal.specsy.{Spec, Specsy}

@RunWith(classOf[Specsy])
class HelloWorldSpec extends Spec {

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
</pre>

You can add test code to any of the blocks between curly braces - semantically there is no difference between the top-level spec and all the nested child specs. There can be as many or few nested specs as you wish (including zero). A child spec will see the side-effects of its parent specs, but it cannot see any side-effects from its sibling specs (see [Isolated Execution Model](#isolated_execution_model)). Potentially every leaf child spec may be executed in its own thread (not yet implemented - a better test runner than JUnit is needed first, i.e. [Jumi](http://jumi.fi/)).

Specsy does not contain its own assertion syntax, so you can use the assertions from [JUnit](http://www.junit.org/), [Hamcrest](http://code.google.com/p/hamcrest/), [specs](http://code.google.com/p/specs/), [specs2](http://specs2.org/), [ScalaTest](http://www.scalatest.org/) or any other framework which makes it possible (see [Assertions](#assertions)).


Naming Tests
------------

Write the name of a test as a string in front of the `>>` operator. It is recommended to name the tests using full sentences which describe features. [FibonacciSpec] is an example of how to use descriptive [specification-style](http://blog.orfjackal.net/2010/02/three-styles-of-naming-tests.html) test names:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class FibonacciSpec extends Spec {
  val sequenceLength = 10
  val fib = new Fibonacci().sequence(sequenceLength)
  assertThat(fib.length, is(sequenceLength))

  "The first two Fibonacci numbers are 0 and 1" >> {
    assertThat(fib(0), is(0))
    assertThat(fib(1), is(1))
  }
  "Each remaining number is the sum of the previous two" >> {
    for (i &lt;- 2 until fib.length) {
      assertThat(fib(i), is(fib(i - 1) + fib(i - 2)))
    }
  }
}
</pre>

You can take advantage of the ability to nest tests when writing the test names, for example as in [StackSpec].


Assertions
----------

To use the assertions from [JUnit](http://www.junit.org/), add the following import to your test file:

<pre class="brush: scala">
import org.junit.Assert._
</pre>

To use the assertions from [Hamcrest](http://code.google.com/p/hamcrest/), add the following imports to your test file:

<pre class="brush: scala">
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
</pre>

To use the assertions from [specs](http://code.google.com/p/specs/), mix in one of the traits mentioned in [specs' matchers guide](http://code.google.com/p/specs/wiki/MatchersGuide#Use_specs_matchers_alone). For example:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class SomeSpec extends Spec with SpecsMatchers {
}
</pre>

To use the assertions from [specs2](http://etorreborre.github.com/specs2/), mix in one of the exception throwing traits mentioned in [specs2's mathers guide](http://etorreborre.github.com/specs2/guide/org.specs2.guide.Matchers.html#Reusing+matchers+outside+of+specs2).

To use the assertions from [ScalaTest](http://www.scalatest.org/), mix in the [org.scalatest.matchers.ShouldMatchers](http://www.scalatest.org/scaladoc-1.5/org/scalatest/matchers/ShouldMatchers.html) trait or one of the other matcher traits:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class SomeSpec extends Spec with ShouldMatchers {
}
</pre>

Any other assertions are also OK. All that is needed is that they throw an exception when the assertion fails. Refer to the documentation of other testing frameworks for instructions on how to use their assertions in another framework.


Isolated Execution Model
------------------------

[StackSpec] illustrates the isolated execution model. As you notice, the stack is a mutable data structure and it is being modified in nearly every child spec. But each child spec can trust that it sees only the modifications made in its parent specs, so there are no weird order-dependent test failures - everything just works as expected. Specsy accomplishes this by creating multiple fresh instances of the test class and selectively executing the nested specs.

<pre class="brush: scala">
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
</pre>

A rule of thumb is that out of all sibling specs (i.e. child specs with the same parent) always *exactly one sibling spec is executed during a test run*, and each test run has its own instance of the test class. So when the closure of a spec is executed and Specsy encounters a child spec declaration, it will selectively execute one of its child specs (right where it is declared) and skip the others. Then a fresh instance of the test class is created and a different code path is executed, until all child specs have been executed.


Non-Isolated Execution Model
----------------------------

In some cases it may be desirable to avoid the isolation of side-effects; perhaps it would make the tests harder to organize (e.g. writing tests for a multi-step process) or it would affect performance too much (e.g. side-effect free parameterized tests). For those situations you may call `shareSideEffects()` which will cause all child specs of the current spec to see each other's side-effects. [ShareSideEffectsExampleSpec] illustrates this:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class ShareSideEffectsExampleSpec extends Spec {
  var counter = 0

  // Without the call to `shareSideEffects()` the value of `counter` would be `1`
  // in the asserts of each of the following child specs.
  shareSideEffects()
  "One" >> {
    counter += 1
    assertThat(counter, is(1))
  }
  "Two" >> {
    counter += 1
    assertThat(counter, is(2))
  }
  "Three" >> {
    counter += 1
    assertThat(counter, is(3))
  }
}
</pre>

Note that the effects of `shareSideEffects()` (pun intended) are restricted inside the subtree of the current spec, after the call to `shareSideEffects()`. The subtree can start from the root spec (if `shareSideEffects()` is called at the top level before all nested specs), or it can start from *any* nested spec. So you can mix the isolated and non-isolated modes inside one test class, so that only one subtree of specs is affected by it (or any number of subtrees, for that matter).

[See here](https://github.com/orfjackal/dimdwarf/blob/e0f109dcd2d81f35b411fd1a2ad75be7ef60ae75/dimdwarf-core/src/test/scala/net/orfjackal/dimdwarf/domain/SimpleTimestampSpec.scala#L64) for a real-life example of using `shareSideEffects()` as a performance optimization for parameterized tests. Actually this is the first use case which drove me to finally implement `shareSideEffects()` (and it took [just one hour to implement](https://github.com/orfjackal/specsy/commit/c31f8508969098b05f7bedac0a9fe9a1b6fe833a)). Before that I had used Specsy for 9 months without any need for it, even though it had been in my plans already since much earlier in [GoSpec](https://github.com/orfjackal/gospec). So it should very rarely, if ever, be necessary to use the non-isolated execution model.


"Before" and "After" Blocks
---------------------------

In Specsy, every parent spec acts similar to the "before" blocks in other testing frameworks. And as for "after" blocks, Specsy has a construct called *defer blocks* (influenced by [Go's defer statement](http://golang.org/doc/effective_go.html#defer)). Each spec can declare as many or few defer blocks as it wishes, and they will be executed in LIFO order when the spec exits.

[DeferBlocksExampleSpec] shows how the defer blocks can be used:

<pre class="brush: scala">
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
</pre>

The code duplication in the above spec could be removed by extracting a method out of it, although it requires knowledge of Scala's more advanced features. [DeferBlocksExample2Spec] does the same thing as above, but with less code:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class DeferBlocksExample2Spec extends Spec {
  val dir = createWithCleanup(new File("temp-directory-" + UUID.randomUUID()), _.mkdir(), _.delete())
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
</pre>


Parameterized Tests
-------------------

Because Specsy's spec declarations are implemented as method calls which take a closure as a parameter (see [Spec]), it's simple to use the framework for parameterized tests. [ParameterizedExampleSpec] shows how to do it:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class ParameterizedExampleSpec extends Spec {
  val parameters = List(
    (0, 0),
    (1, 1),
    (2, 4),
    (3, 9),
    (4, 16),
    (5, 25),
    (6, 36),
    (7, 49),
    (8, 64),
    (9, 81))

  for ((n, expectedSquare) &lt;- parameters) {
    "Square of " + n + " is " + expectedSquare >> {
      assertThat(n * n, is(expectedSquare))
    }
  }
}
</pre>

Note that the code which declares the specs must be deterministic. Otherwise the test isolation mechanism may not run all specs exactly once. Also here it might be desirable to use `shareSideEffects()` as a performance optimization, assuming that the generated specs do not have side-effects.


Executing Tests Only in Some Environments
-----------------------------------------

Since in Specsy every spec is a closure, it is very easy to customize how individual specs are run. For example, let's say that some of the tests require Java 7 to be able to run. You can write a helper method such as the `worksOnlyOnJava7` in [EnvironmentFilterExampleSpec], as shown below, and mark/surround the closures of affected specs with it.

<pre class="brush: scala">
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
</pre>


“Pending Until Fixed”
---------------------

A common situation in Acceptance Test Driven Development (ATDD) is that you have an acceptance test which is not yet passing, until you implement the feature specified by that acceptance test, but that might take a long time. In that situation it can be useful to make the test so that if the test throws an exception, then you don't fail it but consider it to be pending, but if the test does *not* throw an exception and the feature is complete, then you fail it so that you would remember to remove the pending-until-fixed tag.

Specsy does not (yet) have a concept of "pending", but you can achieve almost the same thing by making the test pass and by having the `pendingUntilFixed` method in a helper class which all tests use, so that it's easy to seach for all its usages to find out which tests are pending. [PendingUntilFixedExampleSpec] illustrates this:

<pre class="brush: scala">
@RunWith(classOf[Specsy])
class PendingUntilFixedExampleSpec extends Spec {

  "An acceptance test for an already implemented feature" >> {
    // Test code...
  }

  "An acceptance test whose feature has not yet been implemented" >> AcceptanceTestHelpers.pendingUntilFixed {
    // Test code which is still failing...
    assert(false, "this feature is not implemented")
  }
}

object AcceptanceTestHelpers {

  // When this method is in a helper class, it's easy to find all pending tests
  // by searching for all usages of this method with your IDE.
  def pendingUntilFixed(closure: => Unit) {
    try {
      closure
    } catch {
      case e =>
        System.err.println("This test is pending until fixed:")
        e.printStackTrace()
        return // test is pending
    }
    throw new AssertionError("This test would now pass. Remove the 'pendingUntilFixed' tag.")
  }
}
</pre>


[Spec]:                         http://github.com/orfjackal/specsy/blob/master/src/main/scala/net/orfjackal/specsy/Spec.scala
[FibonacciSpec]:                http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/FibonacciSpec.scala
[StackSpec]:                    http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/StackSpec.scala
[ShareSideEffectsExampleSpec]:  http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/ShareSideEffectsExampleSpec.scala
[DeferBlocksExampleSpec]:       http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/DeferBlocksExampleSpec.scala
[DeferBlocksExample2Spec]:      http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/DeferBlocksExample2Spec.scala
[ParameterizedExampleSpec]:     http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/ParameterizedExampleSpec.scala
[EnvironmentFilterExampleSpec]: http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/EnvironmentFilterExampleSpec.scala
[PendingUntilFixedExampleSpec]: http://github.com/orfjackal/specsy/blob/master/src/test/scala/net/orfjackal/specsy/examples/PendingUntilFixedExampleSpec.scala
