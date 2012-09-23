// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GlobalSpy {

    private static List<String> spy = new ArrayList<>();

    public static void reset() {
        spy.clear();
    }

    public static void add(String message) {
        spy.add(message);
    }

    public static void assertContains(String expected) {
        assertThat(spy, hasItem(expected));
    }

    public static void assertContainsInPartialOrder(String... expected) {
        List<String> actual = new ArrayList<>(spy);
        actual.retainAll(asList(expected));
        assertThat(actual, is(asList(expected)));
    }
}