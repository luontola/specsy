// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.java;

import org.specsy.java.JavaSpecsy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
