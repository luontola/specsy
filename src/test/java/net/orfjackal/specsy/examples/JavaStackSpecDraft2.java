// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class JavaStackSpecDraft2 extends JSpec2 {
    // Good:
    // + string names
    // + can throw checked exceptions
    // + reformat works in every IDE
    // Bad:
    // - high syntax noise
    // - exactly this syntax might not be feasible to implement, because run() must be called after the constructor
    //   and that would probably require bytecode manipulation of the surrounding spec
    //
    // The "throws Throwable" in run() implementations could be avoided,
    // but IDEA inserts it automatically and removing it means more work,
    // so this syntax should be evaluated with it.

    private final Deque<String> stack = new ArrayDeque<String>();

    public void run() throws Throwable {

        new spec("An empty stack") {
            public void run() throws Throwable {

                new spec("is empty") {
                    public void run() throws Throwable {
                        assertTrue(stack.isEmpty());
                    }
                };
                new spec("After a push, the stack is no longer empty") {
                    public void run() throws Throwable {
                        stack.push("a push");
                        assertFalse(stack.isEmpty());
                    }
                };
            }
        };
        new spec("When objects have been pushed onto a stack") {
            public void run() throws Throwable {
                stack.push("pushed first");
                stack.push("pushed last");

                new spec("the object pushed last is popped first") {
                    public void run() throws Throwable {
                        String poppedFirst = stack.pop();
                        assertThat(poppedFirst, is("pushed last"));
                    }
                };
                new spec("the object pushed first is popped last") {
                    public void run() throws Throwable {
                        stack.pop();
                        String poppedLast = stack.pop();
                        assertThat(poppedLast, is("pushed first"));
                    }
                };
                new spec("After popping all objects, the stack is empty") {
                    public void run() throws Throwable {
                        stack.pop();
                        stack.pop();
                        assertTrue(stack.isEmpty());
                    }
                };
            }
        };

        // Could also use some other identifier than "spec", to avoid predefined names even more.
        // Some unpronounceable suggestions: $, _, or some weird Unicode character
        // Typing must be easy, so non-ASCII characters are not feasible. The _ symbol is easier to type
        // than the $ symbol on both US and Finnish keyboard layout, so that is preferred.
        //
        // JLS §3.8 Identifiers: http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#40625

        new _("example of using a symbol for declaring nested specs") {
            public void run() throws Throwable {
            }
        };
    }
}

abstract class JSpec2 {

    public abstract void run() throws Throwable;

    protected abstract class spec {
        public final String name;

        public spec(String name) {
            this.name = name;
        }

        public abstract void run() throws Throwable;
    }

    protected abstract class _ {
        public final String name;

        public _(String name) {
            this.name = name;
        }

        public abstract void run() throws Throwable;
    }
}
