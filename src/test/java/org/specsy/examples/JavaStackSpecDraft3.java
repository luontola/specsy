// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class JavaStackSpecDraft3 extends JSpec3 {
    // Good:
    // + string names
    // + low syntax noise
    // + can throw checked exceptions from anonymous initializer blocks (JLS §8.6 Instance Initializers)
    // + reformat works in IDEA
    // Bad:
    // - cannot throw checked exceptions from initializer blocks of named classes (JLS §8.6), so the root spec would need to be a method
    // - reformat might not work in other IDEs
    // Blocker:
    // - this syntax might not be feasible, because it's not possible to execute the nested specs selectively (without class loader magic)

    private final Deque<String> stack = new ArrayDeque<String>();

    {
        new spec("An empty stack") {{
            new spec("is empty") {{
                assertTrue(stack.isEmpty());
            }};
            new spec("After a push, the stack is no longer empty") {{
                stack.push("a push");
                assertFalse(stack.isEmpty());
            }};
        }};
        new spec("When objects have been pushed onto a stack") {{
            stack.push("pushed first");
            stack.push("pushed last");

            new spec("the object pushed last is popped first") {{
                String poppedFirst = stack.pop();
                assertThat(poppedFirst, is("pushed last"));
            }};
            new spec("the object pushed first is popped last") {{
                stack.pop();
                String poppedLast = stack.pop();
                assertThat(poppedLast, is("pushed first"));
            }};
            new spec("After popping all objects, the stack is empty") {{
                stack.pop();
                stack.pop();
                assertTrue(stack.isEmpty());
            }};
        }};
    }

    public void spec() throws Throwable {

        // See JLS §8.6 Instance Initializers
        // http://java.sun.com/docs/books/jls/third_edition/html/classes.html#246032

        new spec("example of throwing a checked exception in the instance initializer block") {{
            throwsACheckedException();
        }};
    }

    private static void throwsACheckedException() throws Exception {
        // "It is a compile-time error if an instance initializer cannot complete normally"
        // so this unconditional throw must be in a method or inside an if block
        throw new Exception("checked exception");
    }
}

abstract class JSpec3 {

    protected abstract class spec {
        public final String name;

        public spec(String name) {
            this.name = name;
        }
    }
}
