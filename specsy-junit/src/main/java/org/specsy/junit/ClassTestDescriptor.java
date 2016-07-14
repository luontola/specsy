// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.specsy.core.Path;

public class ClassTestDescriptor extends AbstractTestDescriptor {

    public static final String SEGMENT_TYPE = "class";

    private final Class<?> testClass;
    private final Path pathToExecute;

    public ClassTestDescriptor(EngineDescriptor parent, Class<?> testClass) {
        this(parent, testClass, Path.ROOT);
    }

    public ClassTestDescriptor(EngineDescriptor parent, Class<?> testClass, Path pathToExecute) {
        super(parent.getUniqueId().append(SEGMENT_TYPE, testClass.getName()), testClass.getSimpleName());
        this.testClass = testClass;
        this.pathToExecute = pathToExecute;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Path getPathToExecute() {
        return pathToExecute;
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
