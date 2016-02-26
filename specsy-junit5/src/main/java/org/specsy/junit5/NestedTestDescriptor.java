// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import fi.jumi.api.drivers.TestId;
import org.junit.gen5.engine.support.descriptor.AbstractTestDescriptor;

import java.util.StringJoiner;

public class NestedTestDescriptor extends AbstractTestDescriptor {

    private final TestId testId;
    private final String name;

    public NestedTestDescriptor(TestId testId, String name) {
        super(toUniqueId(testId));
        this.testId = testId;
        this.name = name;
    }

    private static String toUniqueId(TestId testId) {
        // TODO: should this generated string be prefixed with the parent's unique ID?
        StringJoiner joiner = new StringJoiner(".");
        for (int index : testId.getPath()) {
            joiner.add("" + index);
        }
        return joiner.toString();
    }

    public TestId getTestId() {
        return testId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public boolean isContainer() {
        return true;
    }
}
