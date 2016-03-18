// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.groovy

import org.specsy.groovy.GroovySpecsy

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class StackSpec extends GroovySpecsy {
    def stack = new ArrayDeque<String>()

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
