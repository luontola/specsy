// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.jumi;

import org.specsy.core.Path;
import org.specsy.runner.notification.*;

import java.util.concurrent.Executor;

public class JumiSuiteNotifierAdapter implements SuiteNotifier {

    private final fi.jumi.api.drivers.SuiteNotifier notifier;
    private final Executor executor;

    public JumiSuiteNotifierAdapter(fi.jumi.api.drivers.SuiteNotifier notifier, Executor executor) {
        this.notifier = notifier;
        this.executor = executor;
    }

    @Override
    public void fireTestFound(Path path, String name, Object location) {
        notifier.fireTestFound(path.toTestId(), name);
    }

    @Override
    public void submitTestRun(Runnable testRun) {
        executor.execute(testRun);
    }

    @Override
    public TestNotifier fireTestStarted(Path path) {
        return new JumiTestNotifierAdapter(notifier.fireTestStarted(path.toTestId()));
    }
}
