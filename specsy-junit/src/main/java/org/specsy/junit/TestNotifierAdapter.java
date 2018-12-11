// Copyright © 2010-2018, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import fi.jumi.api.drivers.TestNotifier;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.opentest4j.MultipleFailuresError;

import java.util.LinkedList;
import java.util.List;

public class TestNotifierAdapter implements TestNotifier {
    private final TestDescriptor descriptor;
    private final List<Throwable> failures = new LinkedList<>();
    private EngineExecutionListener listener;

    public TestNotifierAdapter(EngineExecutionListener listener, TestDescriptor descriptor) {
        this.descriptor = descriptor;
        this.listener = listener;
    }

    @Override
    public void fireFailure(Throwable cause) {
        failures.add(cause);
    }

    @Override
    public void fireTestFinished() {
        listener.executionFinished(descriptor, toResult(failures));
    }

    private static TestExecutionResult toResult(List<Throwable> failures) {
        if (failures.isEmpty()) {
            return TestExecutionResult.successful();
        } else {
            return TestExecutionResult.failed(mergeFailures(failures));
        }
    }

    private static Throwable mergeFailures(List<Throwable> failures) {
        if (failures.isEmpty()) {
            return null;
        }
        if (failures.size() == 1) {
            return failures.get(0);
        }
        return new MultipleFailuresError(null, failures);
    }
}
