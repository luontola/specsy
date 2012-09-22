// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core;

import fi.jumi.api.drivers.SuiteNotifier;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

public class SpecClassRunner implements Runnable {

    private final Class<?> testClass;
    private final SuiteNotifier notifier;
    private final Executor executor;

    public SpecClassRunner(Class<?> testClass, SuiteNotifier notifier, Executor executor) {
        this.testClass = testClass;
        this.notifier = notifier;
        this.executor = executor;
    }

    @Override
    public void run() {
        new SpecRun(new TestClassBootstrap(), notifier, executor).run();
    }

    private class TestClassBootstrap implements Spec {
        @Override
        public void run(final Context context) {
            context.bootstrap(testClass.getSimpleName(), new Closure() {
                @Override
                public void run() throws Throwable {
                    ContextDealer.prepare(context);
                    try {
                        testClass.getConstructor().newInstance();
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
                }
            });
        }
    }

}
