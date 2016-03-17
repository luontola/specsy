// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import org.junit.gen5.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.gen5.engine.support.descriptor.EngineDescriptor;
import org.specsy.core.Path;

public class ClassTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;
    private final Path pathToExecute;

    public ClassTestDescriptor(EngineDescriptor parent, Class<?> testClass) {
        this(parent, testClass, Path.ROOT);
    }

    public ClassTestDescriptor(EngineDescriptor parent, Class<?> testClass, Path pathToExecute) {
        super(parent.getUniqueId() + ":" + testClass.getName());
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
    public String getName() {
        return testClass.getName();
    }

    @Override
    public String getDisplayName() {
        return testClass.getSimpleName();
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + getName() + ")";
    }
}
