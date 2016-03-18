// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.java;

import org.specsy.java.JavaSpecsy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FibonacciSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {
        int sequenceLength = 10;
        int[] fib = new Fibonacci().sequence(sequenceLength);
        assertThat(fib.length, is(sequenceLength));

        spec("The first two Fibonacci numbers are 0 and 1", () -> {
            assertThat(fib[0], is(0));
            assertThat(fib[1], is(1));
        });

        spec("Each remaining number is the sum of the previous two", () -> {
            for (int i = 2; i < fib.length; i++) {
                assertThat(fib[i], is(fib[i - 1] + fib[i - 2]));
            }
        });
    }
}
