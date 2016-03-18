// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.groovy

class Fibonacci {
    private int n0 = 0
    private int n1 = 1

    int[] sequence(int count) {
        int[] seq = new int[count]
        for (int i = 0; i < seq.length; i++) {
            seq[i] = next()
        }
        return seq
    }

    int next() {
        int next = n0
        n0 = n1
        n1 = n1 + next
        return next
    }
}
