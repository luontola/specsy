// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import fi.jumi.api.drivers.TestId;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

public class NestedTestDescriptor extends AbstractTestDescriptor {

    public static final String SEGMENT_TYPE = "nested";

    private final TestId testId;

    public NestedTestDescriptor(TestDescriptor parent, TestId testId, String name) {
        super(parent.getUniqueId().append(SEGMENT_TYPE, String.valueOf(testId.getIndex())), name);
        this.testId = testId;
    }

    public TestId getTestId() {
        return testId;
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
