// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import fi.jumi.api.RunVia;
import fi.jumi.api.drivers.Driver;
import org.junit.gen5.engine.*;
import org.junit.gen5.engine.discovery.ClassSelector;
import org.junit.gen5.engine.support.descriptor.EngineDescriptor;
import org.specsy.Specsy;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

import static org.junit.gen5.engine.TestExecutionResult.Status.FAILED;
import static org.junit.gen5.engine.TestExecutionResult.Status.SUCCESSFUL;

public class SpecsyTestEngine implements TestEngine {

    @Override
    public String getId() {
        return "specsy";
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(getId(), "Specsy " + new Specsy().getVersion());

        // TODO: support other test selector types
        for (ClassSelector selector : discoveryRequest.getSelectorsByType(ClassSelector.class)) {
            Class<?> testClass = selector.getTestClass();
            RunVia runVia = testClass.getAnnotation(RunVia.class);
            if (runVia != null && runVia.value() == Specsy.class) {
                engineDescriptor.addChild(new ClassTestDescriptor(engineDescriptor, testClass));
            }
        }
        return engineDescriptor;
    }

    @Override
    public void execute(ExecutionRequest request) {
        execute(request.getRootTestDescriptor(), request.getEngineExecutionListener());
    }

    private void execute(TestDescriptor descriptor, EngineExecutionListener listener) {
        if (descriptor instanceof EngineDescriptor) {
            execute((EngineDescriptor) descriptor, listener);
        } else if (descriptor instanceof ClassTestDescriptor) {
            execute((ClassTestDescriptor) descriptor, listener);
        } else {
            throw new IllegalArgumentException("Unrecognized descriptor: " + descriptor);
        }
    }

    private void execute(EngineDescriptor descriptor, EngineExecutionListener listener) {
        listener.executionStarted(descriptor);
        for (TestDescriptor child : descriptor.getChildren()) {
            execute(child, listener);
        }
        listener.executionFinished(descriptor, new TestExecutionResult(SUCCESSFUL, null));
    }

    private void execute(ClassTestDescriptor descriptor, EngineExecutionListener listener) {
        AsyncThreadlessExecutor executor = new AsyncThreadlessExecutor();
        try {
            Class<?> testClass = descriptor.getTestClass();
            Driver driver = testClass.getAnnotation(RunVia.class).value().newInstance();
            driver.findTests(testClass, new SuiteNotifierAdapter(listener, descriptor), executor);
        } catch (Throwable t) {
            listener.executionStarted(descriptor);
            listener.executionFinished(descriptor, new TestExecutionResult(FAILED,
                    new RuntimeException("Failed to start running tests in " + descriptor.getName(), t)));
        }
        executor.executeUntilDone();
    }

    private static class AsyncThreadlessExecutor implements Executor {
        private final Queue<Runnable> commands = new ConcurrentLinkedQueue<>();

        @Override
        public void execute(Runnable command) {
            commands.add(command);
        }

        public void executeUntilDone() {
            Runnable command;
            while ((command = commands.poll()) != null) {
                command.run();
            }
        }
    }
}
