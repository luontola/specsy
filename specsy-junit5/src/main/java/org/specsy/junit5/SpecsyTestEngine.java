// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import fi.jumi.api.RunVia;
import org.junit.gen5.engine.*;
import org.junit.gen5.engine.discovery.ClassSelector;
import org.junit.gen5.engine.discovery.ClasspathSelector;
import org.junit.gen5.engine.discovery.UniqueIdSelector;
import org.junit.gen5.engine.support.descriptor.EngineDescriptor;
import org.specsy.Specsy;
import org.specsy.bootstrap.ClassSpec;
import org.specsy.core.Path;
import org.specsy.core.SpecRun;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static org.junit.gen5.commons.util.ReflectionUtils.findAllClassesInClasspathRoot;
import static org.junit.gen5.engine.TestExecutionResult.Status.SUCCESSFUL;

public class SpecsyTestEngine implements TestEngine {

    private static final String ENGINE_ID = "specsy";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(getId(), "Specsy");

        // TODO: support PackageSelector
        for (ClassSelector selector : discoveryRequest.getSelectorsByType(ClassSelector.class)) {
            Class<?> testClass = selector.getTestClass();
            if (isSpecsyClass(testClass)) {
                engineDescriptor.addChild(new ClassTestDescriptor(engineDescriptor, testClass));
            }
        }

        for (ClasspathSelector selector : discoveryRequest.getSelectorsByType(ClasspathSelector.class)) {
            for (Class<?> testClass : findAllClassesInClasspathRoot(selector.getClasspathRoot(), SpecsyTestEngine::isSpecsyClass)) {
                engineDescriptor.addChild(new ClassTestDescriptor(engineDescriptor, testClass));
            }
        }

        for (UniqueIdSelector selector : discoveryRequest.getSelectorsByType(UniqueIdSelector.class)) {
            String uniqueId = selector.getUniqueId();
            if (uniqueId.startsWith(ENGINE_ID + ":")) {
                String[] parts = uniqueId.split(":");
                Class<?> testClass = loadClass(parts[1]);
                Path pathToExecute = Path.of(Stream.of(parts)
                        .skip(2)
                        .mapToInt(Integer::parseInt)
                        .toArray());
                engineDescriptor.addChild(new ClassTestDescriptor(engineDescriptor, testClass, pathToExecute));
            }
        }
        return engineDescriptor;
    }

    private static boolean isSpecsyClass(Class<?> testClass) {
        RunVia runVia = testClass.getAnnotation(RunVia.class);
        return runVia != null && runVia.value() == Specsy.class;
    }

    private static Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        ClassSpec spec = new ClassSpec(descriptor.getTestClass());
        Path pathToExecute = descriptor.getPathToExecute();

        SuiteNotifierAdapter notifier = new SuiteNotifierAdapter(listener, descriptor);
        AsyncThreadlessExecutor executor = new AsyncThreadlessExecutor();
        executor.execute(new SpecRun(spec, pathToExecute, notifier, executor));
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
