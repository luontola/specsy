// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.groovy;

import fi.jumi.api.RunVia;
import org.specsy.Specsy;
import org.specsy.bootstrap.ContextDealer;
import org.specsy.core.*;

@RunVia(Specsy.class)
public abstract class GroovySpecsy implements Closure {

    private final Context context = ContextDealer.take();

    /**
     * The root spec containing all child specs.
     */
    @Override
    public abstract void run() throws Throwable;

    /**
     * Declares a child spec.
     */
    public void spec(String name, Runnable spec) {
        context.spec(name, new GroovyClosure(spec));
    }

    /**
     * Defers the execution of a piece of code until the end of the current spec.
     * All deferred closures will be executed in LIFO order when the current spec exits.
     */
    public void defer(Runnable block) {
        context.defer(new GroovyClosure(block));
    }

    /**
     * Makes all child specs of the current spec able to see each other's side-effects.
     */
    public void shareSideEffects() {
        context.shareSideEffects();
    }
}

class GroovyClosure implements Closure {

    // Groovy's `groovy.lang.Closure` implements `Runnable`, so we can completely avoid a compile time
    // dependency on Groovy and thus be protected from binary incompatible changes in Groovy.

    private final Runnable closure;

    public GroovyClosure(Runnable closure) {
        this.closure = closure;
    }

    @Override
    public void run() throws Throwable {
        closure.run();
    }
}
