// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.bootstrap;

import org.specsy.core.*;

import java.lang.reflect.InvocationTargetException;

public class ClassSpec implements Spec {

    private final Class<?> testClass;

    public ClassSpec(Class<?> testClass) {
        this.testClass = testClass;
    }

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
