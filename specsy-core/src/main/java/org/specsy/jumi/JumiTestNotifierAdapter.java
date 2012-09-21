// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.jumi;

import org.specsy.runner.notification.TestNotifier;

public class JumiTestNotifierAdapter implements TestNotifier {

    private final fi.jumi.api.drivers.TestNotifier notifier;

    public JumiTestNotifierAdapter(fi.jumi.api.drivers.TestNotifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void fireFailure(Throwable cause) {
        notifier.fireFailure(cause);
    }

    @Override
    public void fireTestFinished() {
        notifier.fireTestFinished();
    }
}
