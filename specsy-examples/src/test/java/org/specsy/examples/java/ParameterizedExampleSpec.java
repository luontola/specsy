// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.java;

import org.specsy.java.JavaSpecsy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParameterizedExampleSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {
        int[][] parameters = new int[][]{
                {0, 0},
                {1, 1},
                {2, 4},
                {3, 9},
                {4, 16},
                {5, 25},
                {6, 36},
                {7, 49},
                {8, 64},
                {9, 81}
        };
        for (int[] pair : parameters) {
            int n = pair[0];
            int expectedSquare = pair[1];

            spec("Square of " + n + " is " + expectedSquare, () -> {
                assertThat(n * n, is(expectedSquare));
            });
        }
    }
}
