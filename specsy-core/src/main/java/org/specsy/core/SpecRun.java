// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core;

import fi.jumi.api.drivers.SuiteNotifier;

import java.util.concurrent.Executor;

public class SpecRun implements Runnable {

    private final Spec spec;
    private final Path pathToExecute;
    private final SuiteNotifier notifier;
    private final Executor executor;

    public SpecRun(Spec spec, SuiteNotifier notifier, Executor executor) {
        this(spec, Path.ROOT, notifier, executor);
    }

    public SpecRun(Spec spec, Path pathToExecute, SuiteNotifier notifier, Executor executor) {
        this.spec = spec;
        this.pathToExecute = pathToExecute;
        this.notifier = notifier;
        this.executor = executor;
    }

    @Override
    public void run() {
        Context context = executePath(spec, pathToExecute);
        for (Path postponedPath : context.postponedPaths()) {
            executor.execute(new SpecRun(spec, postponedPath, notifier, executor));
        }
    }

    private Context executePath(Spec spec, Path path) {
        Context context = new Context(path, notifier);
        spec.run(context);
        return context;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + spec + ", " + pathToExecute + ")";
    }
}
