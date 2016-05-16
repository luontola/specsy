---
layout: page
---
{% include JB/setup %}

Specsy is a [BDD](http://dannorth.net/introducing-bdd)-style unit-level testing framework for Scala, Groovy, Java and easily any other language on the JVM. It safely <em>isolates mutable state</em>, supports writing self-documenting tests/specifications, and runs all tests in parallel.

Specsy has all the <em>essential features</em> of a unit testing framework and nothing excess. To illustrate Specsy's <em>expressiveness</em>, its public API has only three methods, but they provide functionality that requires about four printed pages of documentation to cover - more than Specsy even has production code.

The following example demonstrates Specsy's isolated execution model.

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

Refer to the [documentation](/documentation) to see more examples and to start using Specsy.

- Mailing list: <http://groups.google.com/group/specsy>
- Release notes: <https://github.com/orfjackal/specsy/blob/master/RELEASE-NOTES.md>
- Source code: <https://github.com/orfjackal/specsy>
- License: [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
- Developer: [Esko Luontola](https://github.com/orfjackal) ([@orfjackal](http://twitter.com/orfjackal))


Project Goals
-------------

- **Unlimited Nesting** - The specs can be organized into a nested hierarchy. This makes it possible to apply [One Assertion Per Test](http://www.artima.com/weblogs/viewpost.jsp?thread=35578) which [isolates](http://agileinaflash.blogspot.com/2009/02/first.html) the reason for a failure, because the specs are very fine-grained. This flexibility also makes writing [specification-style](http://blog.orfjackal.net/2010/02/three-styles-of-naming-tests.html) tests easier.

- **Isolated Execution** - To make it easy to write [repeatable](http://agileinaflash.blogspot.com/2009/02/first.html) tests, each spec is isolated from the side-effects of its sibling specs. By default, each spec will see only the side-effects of its parent specs. Note that Specsy discourages writing non-repeatable fat integration tests, so a [BeforeClass](http://junit.sourceforge.net/javadoc/org/junit/BeforeClass.html)/[AfterClass](http://junit.sourceforge.net/javadoc/org/junit/AfterClass.html) concept is outside the scope of this project (or at a very low priority - I have some ideas on how it could be done elegantly).

- **No Forced Words** - In order to let you choose the best possible test names, Specsy does not impose any [predefined words](http://blog.orfjackal.net/2010/05/choice-of-words-in-testing-frameworks.html) on its users.

- **Simplicity** - Specsy contains only the essential features, but does them well. Having a particular assertion syntax is not essential and it's easy to use the assertions of other testing libraries, so Specsy itself does not have assertions. Also any syntactic sugar is minimized, in order for it to be easy to know what the code does just by looking at it.

- **Parallel Execution** - Running tests [fast](http://agileinaflash.blogspot.com/2009/02/first.html) is a must for using TDD (my pain threshold for recompile and test execution is about 5-10 seconds). With Specsy all tests in a test class can be run in parallel, taking full use of all available CPU cores.
