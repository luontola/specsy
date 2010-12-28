// Copyright © 2010, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JavaStackSpecDraft2 extends JSpec2 {
    // Good:
    // + string names
    // + can throw checked exceptions
    // + reformat works in every IDE
    // Bad:
    // - high syntax noise

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
}
