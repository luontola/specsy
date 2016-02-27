// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy;

import fi.jumi.api.drivers.Driver;
import fi.jumi.api.drivers.SuiteNotifier;
import org.specsy.bootstrap.ClassSpec;
import org.specsy.core.SpecRun;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;

public class Specsy extends Driver {

    @Override
    public void findTests(Class<?> testClass, SuiteNotifier notifier, Executor executor) {
        executor.execute(new SpecRun(new ClassSpec(testClass), notifier, executor));
    }

    public String getVersion() {
        Properties p = new Properties();
        InputStream in = getClass().getResourceAsStream("/META-INF/maven/org.specsy/specsy-core/pom.properties");
        if (in != null) {
            try {
                p.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p.getProperty("version", "<local-build>");
    }
}
