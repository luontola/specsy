// Copyright © 2010-2018, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import fi.jumi.api.drivers.TestId;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

public class NestedTestDescriptor extends AbstractTestDescriptor {

    public static final String SEGMENT_TYPE = "nested";

    public NestedTestDescriptor(TestDescriptor parent, TestId testId, String name) {
        // XXX: using the parent's TestSource because we don't have a more accurate TestSource
        // TODO: get the line number by creating an exception in org.specsy.core.Context.spec, then set a ClassSource here
        super(parent.getUniqueId().append(SEGMENT_TYPE, String.valueOf(testId.getIndex())),
                name,
                parent.getSource().orElse(null));
    }

    @Override
    public Type getType() {
        return Type.CONTAINER_AND_TEST;
    }
}
