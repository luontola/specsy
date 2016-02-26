// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import fi.jumi.api.drivers.TestNotifier;
import org.junit.gen5.engine.EngineExecutionListener;
import org.junit.gen5.engine.TestDescriptor;
import org.junit.gen5.engine.TestExecutionResult;
import org.opentest4j.MultipleFailuresError;

import java.util.LinkedList;
import java.util.List;

import static org.junit.gen5.engine.TestExecutionResult.Status.FAILED;
import static org.junit.gen5.engine.TestExecutionResult.Status.SUCCESSFUL;

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
        return new TestExecutionResult(failures.isEmpty() ? SUCCESSFUL : FAILED, mergeFailures(failures));
    }

    private static Throwable mergeFailures(List<Throwable> failures) {
        if (failures.isEmpty()) {
            return null;
        }
        if (failures.size() == 1) {
            return failures.get(0);
        }
        // XXX: JUnit 5 cannot express the existence of multiple non-assertion failures
        MultipleFailuresError multiple = new MultipleFailuresError(null);
        for (Throwable failure : failures) {
            if (failure instanceof AssertionError) {
                multiple.addFailure((AssertionError) failure);
            } else {
                multiple.addFailure(new AssertionError(failure));
            }
        }
        return multiple;
    }

}
