// Copyright © 2010, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JavaStackSpecDraft1 {
    // Good:
    // + low syntax noise
    // Bad:
    // - restricted identifier names
    // - cannot throw checked exceptions from initializer blocks
    // - reformat does not work in IDEA (because these are not anonymous inner classes?)
    // - reformat might not work in other IDEs
    // - for the runner to differentiate between nested specs and helper classes, extending/annotating the specs might be needed

    private final Deque<String> stack = new ArrayDeque<String>();

    class An_empty_stack {

        class is_empty {
            {
                assertTrue(stack.isEmpty());
            }
        }

        class After_a_push_the_stack_is_no_longer_empty {
            {
                stack.push("a push");
                assertFalse(stack.isEmpty());
            }
        }
    }

    class When_objects_have_been_pushed_onto_a_stack {
        {
            stack.push("pushed first");
            stack.push("pushed last");
        }

        class the_object_pushed_last_is_popped_first {
            {
                String poppedFirst = stack.pop();
                assertThat(poppedFirst, is("pushed last"));
            }
        }

        class the_object_pushed_first_is_popped_last {
            {
                stack.pop();
                String poppedLast = stack.pop();
                assertThat(poppedLast, is("pushed first"));
            }
        }

        class After_popping_all_objects_the_stack_is_empty {
            {
                stack.pop();
                stack.pop();
                assertTrue(stack.isEmpty());
            }
        }
    }
}
