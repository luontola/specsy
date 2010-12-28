// Copyright © 2010, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JavaStackSpecDraft3 extends JSpec3 {
    // Good:
    // + string names
    // + low syntax noise
    // + reformat works in IDEA
    // Bad:
    // - cannot throw checked exceptions from initializer blocks
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
}

abstract class JSpec3 {

    protected abstract class spec {
        public final String name;

        public spec(String name) {
            this.name = name;
        }
    }
}
