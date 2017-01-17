// Copyright © 2010-2017, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy;

import fi.jumi.api.drivers.Driver;
import fi.jumi.api.drivers.SuiteNotifier;
import org.specsy.bootstrap.ClassSpec;
import org.specsy.core.SpecRun;

import java.lang.reflect.Modifier;
import java.util.concurrent.Executor;

public class Specsy extends Driver {

    @Override
    public void findTests(Class<?> testClass, SuiteNotifier notifier, Executor executor) {
        if (Modifier.isAbstract(testClass.getModifiers())) {
            return;
        }
        executor.execute(new SpecRun(new ClassSpec(testClass), notifier, executor));
    }
}
