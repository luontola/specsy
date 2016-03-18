// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.groovy

import org.specsy.groovy.GroovySpecsy

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.is

public class ParameterizedExampleSpec extends GroovySpecsy {
    @Override
    public void run() {
        def parameters = [
                [0, 0],
                [1, 1],
                [2, 4],
                [3, 9],
                [4, 16],
                [5, 25],
                [6, 36],
                [7, 49],
                [8, 64],
                [9, 81]
        ]
        parameters.each { n, expectedSquare ->
            spec "Square of $n is $expectedSquare", {
                assertThat(n * n, is(expectedSquare))
            }
        }
    }
}
