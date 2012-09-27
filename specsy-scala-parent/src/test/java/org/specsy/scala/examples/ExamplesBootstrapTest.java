// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala.examples;

import fi.jumi.core.config.*;
import fi.jumi.launcher.*;
import fi.jumi.launcher.ui.*;
import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ExamplesBootstrapTest {

    // TODO: remove this boostrap when Jumi can be run without it

    private static final boolean DEBUG = false;
    private static final long TIMEOUT = 15 * 1000;

    @Test(timeout = TIMEOUT)
    public void run_using_Jumi() throws Exception {
        List<Class<?>> testClasses = Arrays.<Class<?>>asList(
                DeferBlocksExample2Spec.class,
                DeferBlocksExampleSpec.class,
                EnvironmentFilterExampleSpec.class,
                FibonacciSpec.class,
                HelloWorldSpec.class,
                ParameterizedExampleSpec.class,
                PendingUntilFixedExampleSpec.class,
                ShareSideEffectsExampleSpec.class,
                StackSpec.class
        );
        for (Class<?> testClass : testClasses) {
            runTests(testClass);
            System.out.println();
        }
    }

    private static void runTests(Class<?> testClass) throws IOException, InterruptedException {
        Path javaHome = Paths.get(System.getProperty("java.home"));

        SuiteConfigurationBuilder suite = new SuiteConfigurationBuilder()
                .addJvmOptions("-ea")
                .testClass(testClass.getName());

        for (String library : System.getProperty("java.class.path").split(";")) {
            Path libraryPath = Paths.get(library);
            if (!libraryPath.startsWith(javaHome)) {
                suite.addToClassPath(libraryPath);
            }
        }

        DaemonConfiguration daemon = new DaemonConfigurationBuilder()
                .logActorMessages(DEBUG)
                .freeze();

        try (JumiLauncher launcher = createLauncher()) {
            launcher.start(suite.freeze(), daemon);

            TextUI ui = new TextUI(launcher.getEventStream(), new PlainTextPrinter(System.out));
            ui.updateUntilFinished();

            launcher.close();
        }
    }

    private static JumiLauncher createLauncher() {
        return new JumiLauncherBuilder() {
            @Override
            protected OutputStream createDaemonOutputListener() {
                if (DEBUG) {
                    return System.err;
                } else {
                    return super.createDaemonOutputListener();
                }
            }
        }.build();
    }
}
