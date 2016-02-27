// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import fi.jumi.api.drivers.TestId;
import org.junit.gen5.engine.TestDescriptor;
import org.junit.gen5.engine.support.descriptor.AbstractTestDescriptor;

public class NestedTestDescriptor extends AbstractTestDescriptor {

    private final TestId testId;
    private final String name;

    public NestedTestDescriptor(TestDescriptor parent, TestId testId, String name) {
        super(parent.getUniqueId() + ":" + testId.getIndex());
        this.testId = testId;
        this.name = name;
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
