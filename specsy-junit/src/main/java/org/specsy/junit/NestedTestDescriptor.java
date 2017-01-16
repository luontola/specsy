// Copyright © 2010-2017, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import fi.jumi.api.drivers.TestId;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

public class NestedTestDescriptor extends AbstractTestDescriptor {

    public static final String SEGMENT_TYPE = "nested";

    public NestedTestDescriptor(TestDescriptor parent, TestId testId, String name) {
        super(parent.getUniqueId().append(SEGMENT_TYPE, String.valueOf(testId.getIndex())), name);
        // XXX: using the parent's TestSource because we don't have a more accurate TestSource
        // TODO: get the line number by creating an exception in org.specsy.core.Context.spec, then set a ClassSource here
        parent.getSource().ifPresent(this::setSource);
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
