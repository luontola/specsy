// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JavaStackSpecDraft4 extends JSpec4 {
    // More feasible variation of draft 2
    //
    // Good:
    // + string names
    // + can throw checked exceptions
    // + reformat works in every IDE
    // Bad:
    // - high syntax noise
    //
    // The "throws Throwable" in run() implementations could be avoided,
    // but IDEA inserts it automatically and removing it means more work,
    // so this syntax should be evaluated with it.

    private final Deque<String> stack = new ArrayDeque<String>();

    public void run() throws Throwable {

        spec("An empty stack", new NestedSpec() {
            public void run() throws Throwable {

                spec("is empty", new NestedSpec() {
                    public void run() throws Throwable {
                        assertTrue(stack.isEmpty());
                    }
                });
                spec("After a push, the stack is no longer empty", new NestedSpec() {
                    public void run() throws Throwable {
                        stack.push("a push");
                        assertFalse(stack.isEmpty());
                    }
                });
            }
        });
        spec("When objects have been pushed onto a stack", new _() {
            public void run() throws Throwable {
                stack.push("pushed first");
                stack.push("pushed last");

                spec("the object pushed last is popped first", new _() {
                    public void run() throws Throwable {
                        String poppedFirst = stack.pop();
                        assertThat(poppedFirst, is("pushed last"));
                    }
                });
                spec("the object pushed first is popped last", new _() {
                    public void run() throws Throwable {
                        stack.pop();
                        String poppedLast = stack.pop();
                        assertThat(poppedLast, is("pushed first"));
                    }
                });
                spec("After popping all objects, the stack is empty", new _() {
                    public void run() throws Throwable {
                        stack.pop();
                        stack.pop();
                        assertTrue(stack.isEmpty());
                    }
                });
            }
        });
    }
}

abstract class JSpec4 {

    protected void spec(String name, NestedSpec nestedSpec) {
    }

    public abstract void run() throws Throwable;

    public static abstract class NestedSpec {
        public abstract void run() throws Throwable;
    }

    // shorthand for NestedSpec
    public static abstract class _ extends NestedSpec {
    }
}
