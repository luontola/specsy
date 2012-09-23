// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.java;

import org.specsy.bootstrap.ContextDealer;
import org.specsy.core.*;

public abstract class JavaSpecsy implements Closure {

    private final Context context = ContextDealer.take();

    @Override
    public abstract void run() throws Throwable;

    public void spec(String name, Closure spec) {
        context.specify(name, spec);
    }

    public void defer(Closure block) {
        context.defer(block);
    }

    public void shareSideEffects() {
        context.shareSideEffects();
    }
}
