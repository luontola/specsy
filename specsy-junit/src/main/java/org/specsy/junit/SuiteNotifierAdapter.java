// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import fi.jumi.api.drivers.SuiteNotifier;
import fi.jumi.api.drivers.TestId;
import fi.jumi.api.drivers.TestNotifier;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;

import java.util.HashMap;
import java.util.Map;

public class SuiteNotifierAdapter implements SuiteNotifier {

    private final EngineExecutionListener listener;
    private final Map<TestId, TestDescriptor> descriptorsByTestId = new HashMap<>();

    public SuiteNotifierAdapter(EngineExecutionListener listener, ClassTestDescriptor rootDescriptor) {
        this.listener = listener;
        this.descriptorsByTestId.put(TestId.ROOT, rootDescriptor);
    }

    @Override
    public void fireTestFound(TestId testId, String name) {
        descriptorsByTestId.computeIfAbsent(testId, k -> createTestDescriptor(testId, name));
    }

    private TestDescriptor createTestDescriptor(TestId testId, String name) {
        TestDescriptor parent = descriptorsByTestId.get(testId.getParent());
        NestedTestDescriptor descriptor = new NestedTestDescriptor(parent, testId, name);
        parent.addChild(descriptor);
        listener.dynamicTestRegistered(descriptor);
        return descriptor;
    }

    @Override
    public TestNotifier fireTestStarted(TestId testId) {
        TestDescriptor descriptor = getTestDescriptor(testId);
        listener.executionStarted(descriptor);
        return new TestNotifierAdapter(listener, descriptor);
    }

    private TestDescriptor getTestDescriptor(TestId testId) {
        return descriptorsByTestId.computeIfAbsent(testId, key -> {
            throw new IllegalStateException("key not found: " + key);
        });
    }

    @Override
    public void fireInternalError(String message, Throwable cause) {
        // TODO: proper error handling
        throw new RuntimeException("Internal error: " + message, cause);
    }
}
