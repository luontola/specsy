---
title: Documentation
layout: page
group: navigation
---
{% include JB/setup %}

- [Quick Start](#quick-start)
- [Naming Tests](#naming-tests)
- [Assertions](#assertions)
- [Isolated Execution Model](#isolated-execution-model)
- [Non-Isolated Execution Model](#non-isolated-execution-model)
- [“Before” and “After” Blocks](#before-and-after-blocks)
- [Parameterized Tests](#parameterized-tests)
- [Executing Tests Only in Some Environments](#executing-tests-only-in-some-environments)
- [“Pending Until Fixed”](#pending-until-fixed)


Quick Start
-----------

After you have [configured your dependencies](download.html), you can create a Specsy spec by extending the language specific base class, as shown below. **Specsy 2 requires the [Jumi test runner](http://jumi.fi/), so please refer to Jumi's documentation to find out [how to run the tests](https://github.com/orfjackal/jumi/wiki/Running-Tests).** (If you *have* to use JUnit's test runner instead of Jumi, then have a look at [NestedJUnit](https://github.com/orfjackal/nestedjunit) which supports a quite similar way of writing tests.)

Here are examples of the language specific syntax for all of Specsy's API:

- [Scala](https://github.com/orfjackal/specsy/blob/master/specsy-scala-parent/src/test/scala/org/specsy/scala/ScalaSpecsyExample.scala)
- [Groovy](https://github.com/orfjackal/specsy/blob/master/specsy-groovy/src/test/java/org/specsy/groovy/GroovySpecsyExample.groovy)
- [Java](https://github.com/orfjackal/specsy/blob/master/specsy-java/src/test/java/org/specsy/java/JavaSpecsyExample.java)

For the rest of this documentation we will use the Scala version's shorthand syntax. The following shows the structure of a spec:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
import org.specsy.scala.ScalaSpecsy

class HelloWorldSpec extends ScalaSpecsy {

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
<pre class="brush: groovy" data-language="Groovy">
import org.specsy.groovy.GroovySpecsy

class HelloWorldSpec extends GroovySpecsy {
    @Override
    void run() {

        // top-level spec; add your test code here and/or the child specs

        spec "name of the spec", {
            // first child spec
        }

        spec "name of the spec", {
            // second child spec

            spec "name of the spec", {
                // a nested child spec
            }
        }
    }
}
</pre>
<pre class="brush: java" data-language="Java">
import org.specsy.java.JavaSpecsy;

public class HelloWorldSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {

        // top-level spec; add your test code here and/or the child specs

        spec("name of the spec", () -> {
            // first child spec
        });

        spec("name of the spec", () -> {
            // second child spec

            spec("name of the spec", () -> {
                // a nested child spec
            });
        });
    }
}
</pre>
</figure>

You can add test code to any of the blocks between curly braces - semantically there is no difference between the top-level spec and all the nested child specs. There can be as many or few nested specs as you wish (including zero). A child spec will see the side-effects of its parent specs, but it cannot see any side-effects from its sibling specs (see [Isolated Execution Model](#isolated-execution-model)). The test runner can run each leaf child spec in its own thread.

Specsy does not contain its own assertion syntax, so you can use the assertions from [JUnit](http://www.junit.org/), [Hamcrest](http://code.google.com/p/hamcrest/), [specs](http://code.google.com/p/specs/), [specs2](http://specs2.org/), [ScalaTest](http://www.scalatest.org/) or any other framework which makes it possible (see [Assertions](#assertions)).


Naming Tests
------------

It is recommended to name the tests using full sentences which describe features. [FibonacciSpec] is an example of how to use descriptive [specification-style](http://blog.orfjackal.net/2010/02/three-styles-of-naming-tests.html) test names:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class FibonacciSpec extends ScalaSpecsy {
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
<pre class="brush: groovy" data-language="Groovy">
class FibonacciSpec extends GroovySpecsy {
    @Override
    void run() {
        int sequenceLength = 10
        int[] fib = new Fibonacci().sequence(sequenceLength)
        assertThat(fib.length, is(sequenceLength))

        spec "The first two Fibonacci numbers are 0 and 1", {
            assertThat(fib[0], is(0))
            assertThat(fib[1], is(1))
        }

        spec "Each remaining number is the sum of the previous two", {
            for (int i = 2; i &lt; fib.length; i++) {
                assertThat(fib[i], is(fib[i - 1] + fib[i - 2]))
            }
        }
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class FibonacciSpec extends JavaSpecsy {
    @Override
    public void run() {
        int sequenceLength = 10;
        int[] fib = new Fibonacci().sequence(sequenceLength);
        assertThat(fib.length, is(sequenceLength));

        spec("The first two Fibonacci numbers are 0 and 1", () -> {
            assertThat(fib[0], is(0));
            assertThat(fib[1], is(1));
        });

        spec("Each remaining number is the sum of the previous two", () -> {
            for (int i = 2; i &lt; fib.length; i++) {
                assertThat(fib[i], is(fib[i - 1] + fib[i - 2]));
            }
        });
    }
}
</pre>
</figure>

You can take advantage of the ability to nest tests when writing the test names, for example as in [StackSpec].


Assertions
----------

To use the assertions from [JUnit](http://www.junit.org/), add the following import to your test file:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
import org.junit.Assert._
</pre>
<pre class="brush: groovy" data-language="Groovy">
import static org.junit.Assert.*
</pre>
<pre class="brush: java" data-language="Java">
import static org.junit.Assert.*;
</pre>
</figure>

To use the assertions from [Hamcrest](http://code.google.com/p/hamcrest/), add the following imports to your test file:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
</pre>
<pre class="brush: groovy" data-language="Groovy">
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
</pre>
<pre class="brush: java" data-language="Java">
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
</pre>
</figure>

To use the assertions from [specs](http://code.google.com/p/specs/), mix in one of the traits mentioned in [specs' matchers guide](http://code.google.com/p/specs/wiki/MatchersGuide#Use_specs_matchers_alone). For example:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class SomeSpec extends ScalaSpecsy with SpecsMatchers {
}
</pre>
</figure>

To use the assertions from [specs2](http://etorreborre.github.com/specs2/), mix in one of the exception throwing traits mentioned in [specs2's mathers guide](http://etorreborre.github.com/specs2/guide/org.specs2.guide.Matchers.html#Outside+specs2).

To use the assertions from [ScalaTest](http://www.scalatest.org/), mix in the [org.scalatest.matchers.ShouldMatchers](http://doc.scalatest.org/1.8/index.html#org.scalatest.matchers.ShouldMatchers) trait or one of the other matcher traits:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class SomeSpec extends ScalaSpecsy with ShouldMatchers {
}
</pre>
</figure>

Any other assertions are also OK. All that is needed is that they throw an exception when the assertion fails. Refer to the documentation of other testing frameworks for instructions on how to use their assertions in another framework.


Isolated Execution Model
------------------------

[StackSpec] illustrates the isolated execution model. As you notice, the stack is a mutable data structure and it is being modified in nearly every child spec. But each child spec can trust that it sees only the modifications made in its parent specs, so there are no weird order-dependent test failures - everything just works as expected. It's kind of like *lexical scoping* applied to side-effects. Specsy accomplishes this by creating multiple fresh instances of the test class and selectively executing the nested specs.

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class StackSpec extends ScalaSpecsy {
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
<pre class="brush: groovy" data-language="Groovy">
class StackSpec extends GroovySpecsy {
    def stack = new ArrayDeque&lt;String>()

    @Override
    void run() {

        spec "An empty stack", {

            spec "is empty", {
                assertTrue(stack.isEmpty())
            }
            spec "After a push, the stack is no longer empty", {
                stack.push("a push")
                assertFalse(stack.isEmpty())
            }
        }

        spec "When objects have been pushed onto a stack", {
            stack.push("pushed first")
            stack.push("pushed last")

            spec "the object pushed last is popped first", {
                String poppedFirst = stack.pop()
                assertThat(poppedFirst, is("pushed last"))
            }
            spec "the object pushed first is popped last", {
                stack.pop()
                String poppedLast = stack.pop()
                assertThat(poppedLast, is("pushed first"))
            }
            spec "After popping all objects, the stack is empty", {
                stack.pop()
                stack.pop()
                assertTrue(stack.isEmpty())
            }
        }
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class StackSpec extends JavaSpecsy {
    private Deque&lt;String> stack = new ArrayDeque&lt;>();

    @Override
    public void run() {

        spec("An empty stack", () -> {

            spec("is empty", () -> {
                assertTrue(stack.isEmpty());
            });
            spec("After a push, the stack is no longer empty", () -> {
                stack.push("a push");
                assertFalse(stack.isEmpty());
            });
        });

        spec("When objects have been pushed onto a stack", () -> {
            stack.push("pushed first");
            stack.push("pushed last");

            spec("the object pushed last is popped first", () -> {
                String poppedFirst = stack.pop();
                assertThat(poppedFirst, is("pushed last"));
            });
            spec("the object pushed first is popped last", () -> {
                stack.pop();
                String poppedLast = stack.pop();
                assertThat(poppedLast, is("pushed first"));
            });
            spec("After popping all objects, the stack is empty", () -> {
                stack.pop();
                stack.pop();
                assertTrue(stack.isEmpty());
            });
        });
    }
}
</pre>
</figure>

A rule of thumb is that out of all sibling specs (i.e. child specs with the same parent) always *exactly one sibling spec is executed during a test run*, and each test run has its own instance of the test class. So when the closure of a spec is executed and Specsy encounters a child spec declaration, it will selectively execute one of its child specs (right where it is declared) and skip the others. Then a fresh instance of the test class is created and a different code path is executed, until all child specs have been executed.


Non-Isolated Execution Model
----------------------------

In some cases it may be desirable to avoid the isolation of side-effects; perhaps it would make the tests harder to organize (e.g. writing tests for a multi-step process) or it would affect performance too much (e.g. side-effect free parameterized tests). For those situations you may call `shareSideEffects()` which will cause all child specs of the current spec to see each other's side-effects. [ShareSideEffectsExampleSpec] illustrates this:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class ShareSideEffectsExampleSpec extends ScalaSpecsy {
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
<pre class="brush: groovy" data-language="Groovy">
class ShareSideEffectsExampleSpec extends GroovySpecsy {
    def counter = 0

    @Override
    void run() {

        // Without the call to `shareSideEffects()` the value of `counter` would be `1`
        // in the asserts of each of the following child specs.
        shareSideEffects()
        spec "One", {
            counter += 1
            assertThat(counter, is(1))
        }
        spec "Two", {
            counter += 1
            assertThat(counter, is(2))
        }
        spec "Three", {
            counter += 1
            assertThat(counter, is(3))
        }
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class ShareSideEffectsExampleSpec extends JavaSpecsy {
    private int counter = 0;

    @Override
    public void run() {

        // Without the call to `shareSideEffects()` the value of `counter` would be `1`
        // in the asserts of each of the following child specs.
        shareSideEffects();
        spec("One", () -> {
            counter += 1;
            assertThat(counter, is(1));
        });
        spec("Two", () -> {
            counter += 1;
            assertThat(counter, is(2));
        });
        spec("Three", () -> {
            counter += 1;
            assertThat(counter, is(3));
        });
    }
}
</pre>
</figure>

Note that the effects of `shareSideEffects()` (pun intended) are restricted inside the subtree of the current spec, after the call to `shareSideEffects()`. The subtree can start from the root spec (if `shareSideEffects()` is called at the top level before all nested specs), or it can start from *any* nested spec. So you can mix the isolated and non-isolated modes inside one test class, so that only one subtree of specs is affected by it (or any number of subtrees, for that matter).

[See here](https://github.com/orfjackal/dimdwarf/blob/e0f109dcd2d81f35b411fd1a2ad75be7ef60ae75/dimdwarf-core/src/test/scala/net/orfjackal/dimdwarf/domain/SimpleTimestampSpec.scala#L64) for a real-life example of using `shareSideEffects()` as a performance optimization for parameterized tests. Actually this is the first use case which drove me to finally implement `shareSideEffects()` (and it took [just one hour to implement](https://github.com/orfjackal/specsy/commit/c31f8508969098b05f7bedac0a9fe9a1b6fe833a)). Before that I had used Specsy for 9 months without any need for it, even though it had been in my plans already since much earlier in [GoSpec](https://github.com/orfjackal/gospec). So it should very rarely, if ever, be necessary to use the non-isolated execution model.


"Before" and "After" Blocks
---------------------------

In Specsy, every parent spec acts similar to the "before" blocks in other testing frameworks. And as for "after" blocks, Specsy has a construct called *defer blocks* (inspired by [Go's defer statement](http://golang.org/doc/effective_go.html#defer)). Each spec can declare as many or few defer blocks as it wishes, and they will be executed in LIFO order when the spec exits.

[DeferBlocksExampleSpec] shows how the defer blocks can be used:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
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
</pre>
<pre class="brush: groovy" data-language="Groovy">
class DeferBlocksExampleSpec extends GroovySpecsy {
    @Override
    void run() {
        Path dir = Paths.get("temp-directory-" + UUID.randomUUID())
        Files.createDirectory(dir)
        defer {
            Files.delete(dir)
        }

        Path file1 = dir.resolve("file 1.txt")
        Files.createFile(file1)
        defer {
            Files.delete(file1)
        }

        spec "...", {
            // do something with the files
        }

        spec "...", {
            // child specs can also use defer blocks
            Path file2 = dir.resolve("file 2.txt")
            Files.createFile(file2)
            defer {
                Files.delete(file2)
            }

            // 'file2' will be deleted when this child spec exits
        }
        // will delete first 'file1' and second 'dir'
        // (or if creating 'file1' failed, then will delete only 'dir')
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class DeferBlocksExampleSpec extends JavaSpecsy {
    @Override
    public void run() throws IOException {
        Path dir = Paths.get("temp-directory-" + UUID.randomUUID());
        Files.createDirectory(dir);
        defer(() -> {
            Files.delete(dir);
        });

        Path file1 = dir.resolve("file 1.txt");
        Files.createFile(file1);
        defer(() -> {
            Files.delete(file1);
        });

        spec("...", () -> {
            // do something with the files
        });

        spec("...", () -> {
            // child specs can also use defer blocks
            Path file2 = dir.resolve("file 2.txt");
            Files.createFile(file2);
            defer(() -> {
                Files.delete(file2);
            });

            // 'file2' will be deleted when this child spec exits
        });
        // will delete first 'file1' and second 'dir'
        // (or if creating 'file1' failed, then will delete only 'dir')
    }
}
</pre>
</figure>

The code duplication in the above spec could be removed by extracting a method out of it, although it requires knowledge of Scala's more advanced features. [DeferBlocksExample2Spec] does the same thing as above, but with less code:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
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
</pre>
<pre class="brush: groovy" data-language="Groovy">
class DeferBlocksExample2Spec extends GroovySpecsy {
    @Override
    void run() {
        Path dir = createWithCleanup(Paths.get("temp-directory-" + UUID.randomUUID()), Files.&createDirectory)
        Path file1 = createWithCleanup(dir.resolve("file 1.txt"), Files.&createFile)

        spec "...", {
        }

        spec "...", {
            Path file2 = createWithCleanup(dir.resolve("file 2.txt"), Files.&createFile)
        }
    }

    private Path createWithCleanup(Path path, Closure create) {
        println "Creating $path"
        create(path)
        defer {
            println "Deleting $path"
            Files.delete(path)
        }
        return path
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class DeferBlocksExample2Spec extends JavaSpecsy {
    @Override
    public void run() throws IOException {
        Path dir = createWithCleanup(Paths.get("temp-directory-" + UUID.randomUUID()), Files::createDirectory);
        Path file1 = createWithCleanup(dir.resolve("file 1.txt"), Files::createFile);

        spec("...", () -> {
        });

        spec("...", () -> {
            Path file2 = createWithCleanup(dir.resolve("file 2.txt"), Files::createFile);
        });
    }

    private Path createWithCleanup(Path path, FileCreator creator) throws IOException {
        System.out.println("Creating " + path);
        creator.create(path);
        defer(() -> {
            System.out.println("Deleting " + path);
            Files.delete(path);
        });
        return path;
    }

    private interface FileCreator {
        void create(Path path) throws IOException;
    }
}
</pre>
</figure>


Parameterized Tests
-------------------

Because Specsy's spec declarations are implemented as method calls which take a closure as a parameter, it's simple to use the framework for parameterized tests. [ParameterizedExampleSpec] shows how to do it:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class ParameterizedExampleSpec extends ScalaSpecsy {
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
    s"Square of $n is $expectedSquare" >> {
      assertThat(n * n, is(expectedSquare))
    }
  }
}
</pre>
<pre class="brush: groovy" data-language="Groovy">
public class ParameterizedExampleSpec extends GroovySpecsy {
    @Override
    public void run() {
        def parameters = [
                [0, 0],
                [1, 1],
                [2, 4],
                [3, 9],
                [4, 16],
                [5, 25],
                [6, 36],
                [7, 49],
                [8, 64],
                [9, 81]
        ]
        parameters.each { n, expectedSquare ->
            spec "Square of $n is $expectedSquare", {
                assertThat(n * n, is(expectedSquare))
            }
        }
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class ParameterizedExampleSpec extends JavaSpecsy {
    @Override
    public void run() {
        int[][] parameters = new int[][]{
                {0, 0},
                {1, 1},
                {2, 4},
                {3, 9},
                {4, 16},
                {5, 25},
                {6, 36},
                {7, 49},
                {8, 64},
                {9, 81}
        };
        for (int[] pair : parameters) {
            int n = pair[0];
            int expectedSquare = pair[1];

            spec("Square of " + n + " is " + expectedSquare, () -> {
                assertThat(n * n, is(expectedSquare));
            });
        }
    }
}
</pre>
</figure>

Note that the code which declares the specs must be deterministic. Otherwise the test isolation mechanism may not run all specs exactly once. Also here it might be desirable to use `shareSideEffects()` as a performance optimization, assuming that the generated specs do not have side-effects.


Executing Tests Only in Some Environments
-----------------------------------------

Since in Specsy every spec is a closure, it is very easy to customize how individual specs are run. For example, let's say that some of the tests require Java 8 to be able to run. You can write a helper method such as the `worksOnlyOnJava8` in [EnvironmentFilterExampleSpec], as shown below, and mark/surround the closures of affected specs with it.

<figure class="example">
<pre class="brush: scala" data-language="Scala">
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
</pre>
<pre class="brush: groovy" data-language="Groovy">
class EnvironmentFilterExampleSpec extends GroovySpecsy {
    @Override
    void run() {
        spec "This test is run every time", {
            // Test code...
        }

        spec "This test is run only under Java 8 and greater", {
            worksOnlyOnJava8 {
                // Test code... For example something which uses the new Date and Time API (JSR-310)
                // which was added in Java 8
            }
        }

        // This can also be used at the top level, if many/all tests work only on Java 8.
        // Just surround all tests and variable/field declarations into a closure.
        worksOnlyOnJava8 {

            spec "This requires Java 8", {
                // Test code...
            }
            spec "This also requires Java 8", {
                // Test code...
            }
        }
    }

    private static void worksOnlyOnJava8(Closure closure) {
        if (isJava8()) {
            closure.run()
        }
    }

    private static boolean isJava8() {
        try {
            Class.forName("java.time.LocalDate")
            return true
        } catch (ClassNotFoundException e) {
            return false
        }
    }
}
</pre>
<pre class="brush: java" data-language="Java">
public class EnvironmentFilterExampleSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {
        spec("This test is run every time", () -> {
            // Test code...
        });

        spec("This test is run only under Java 8 and greater", () -> worksOnlyOnJava8(() -> {
            // Test code... For example something which uses the new Date and Time API (JSR-310)
            // which was added in Java 8
        }));

        // This can also be used at the top level, if many/all tests work only on Java 8.
        // Just surround all tests and variable/field declarations into a closure.
        worksOnlyOnJava8(() -> {

            spec("This requires Java 8", () -> {
                // Test code...
            });
            spec("This also requires Java 8", () -> {
                // Test code...
            });
        });
    }

    private static void worksOnlyOnJava8(Closure closure) throws Throwable {
        if (isJava8()) {
            closure.run();
        }
    }

    private static boolean isJava8() {
        try {
            Class.forName("java.time.LocalDate");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
</pre>
</figure>


“Pending Until Fixed”
---------------------

A common situation in Acceptance Test Driven Development (ATDD) is that you have an acceptance test which is not yet passing, until you implement the feature specified by that acceptance test, but that might take a long time. In that situation it can be useful to make the test so that if the test throws an exception, then you don't fail it but consider it to be pending, but if the test does *not* throw an exception and the feature is complete, then you fail it so that you would remember to remove the pending-until-fixed tag.

Specsy does not (yet) have a concept of "pending", but you can achieve almost the same thing by making the test pass and by having the `pendingUntilFixed` method in a helper class which all tests use, so that it's easy to seach for all its usages to find out which tests are pending. [PendingUntilFixedExampleSpec] illustrates this:

<figure class="example">
<pre class="brush: scala" data-language="Scala">
class PendingUntilFixedExampleSpec extends ScalaSpecsy {

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
      case e: Throwable =>
        System.err.println("This test is pending until fixed:")
        e.printStackTrace()
        return // test is pending
    }
    throw new AssertionError("This test would now pass. Remove the 'pendingUntilFixed' tag.")
  }
}
</pre>
<pre class="brush: groovy" data-language="Groovy">
import static com.example.AcceptanceTestHelpers.pendingUntilFixed

class PendingUntilFixedExampleSpec extends GroovySpecsy {
    @Override
    void run() {
        spec "An acceptance test for an already implemented feature", {
            // Test code...
        }

        spec "An acceptance test whose feature has not yet been implemented", {
            pendingUntilFixed {
                // Test code which is still failing...
                throw new AssertionError("this feature is not implemented")
            }
        }
    }
}

class AcceptanceTestHelpers {

    // When this method is in a helper class, it's easy to find all pending tests
    // by searching for all usages of this method with your IDE.
    public static void pendingUntilFixed(Closure closure) {
        try {
            closure.run()
        } catch (Throwable t) {
            System.err.println("This test is pending until fixed:")
            t.printStackTrace()
            return // test is pending
        }
        throw new AssertionError("This test would now pass. Remove the 'pendingUntilFixed' tag.")
    }
}
</pre>
<pre class="brush: java" data-language="Java">
import static com.example.AcceptanceTestHelpers.pendingUntilFixed;

public class PendingUntilFixedExampleSpec extends JavaSpecsy {
    @Override
    public void run() {
        spec("An acceptance test for an already implemented feature", () -> {
            // Test code...
        });

        spec("An acceptance test whose feature has not yet been implemented", () -> pendingUntilFixed(() -> {
            // Test code which is still failing...
            throw new AssertionError("this feature is not implemented");
        }));
    }
}

class AcceptanceTestHelpers {

    // When this method is in a helper class, it's easy to find all pending tests
    // by searching for all usages of this method with your IDE.
    public static void pendingUntilFixed(Closure closure) {
        try {
            closure.run();
        } catch (Throwable t) {
            System.err.println("This test is pending until fixed:");
            t.printStackTrace();
            return; // test is pending
        }
        throw new AssertionError("This test would now pass. Remove the 'pendingUntilFixed' tag.");
    }
}
</pre>
</figure>


[FibonacciSpec]:                https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/FibonacciSpec.scala
[StackSpec]:                    https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/StackSpec.scala
[ShareSideEffectsExampleSpec]:  https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/ShareSideEffectsExampleSpec.scala
[DeferBlocksExampleSpec]:       https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/DeferBlocksExampleSpec.scala
[DeferBlocksExample2Spec]:      https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/DeferBlocksExample2Spec.scala
[ParameterizedExampleSpec]:     https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/ParameterizedExampleSpec.scala
[EnvironmentFilterExampleSpec]: https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/EnvironmentFilterExampleSpec.scala
[PendingUntilFixedExampleSpec]: https://github.com/orfjackal/specsy/blob/master/specsy-examples/src/test/scala/org/specsy/examples/scala/PendingUntilFixedExampleSpec.scala
