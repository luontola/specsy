// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit5;

import org.junit.Test;
import org.junit.gen5.launcher.Launcher;
import org.junit.gen5.launcher.listeners.LoggingListener;
import org.junit.gen5.launcher.listeners.SummaryGeneratingListener;
import org.junit.gen5.launcher.listeners.TestExecutionSummary;
import org.junit.gen5.launcher.main.LauncherFactory;
import org.junit.gen5.launcher.main.TestDiscoveryRequestBuilder;
import org.specsy.java.JavaSpecsy;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.gen5.engine.discovery.ClassSelector.forClass;

public class SpecsyTestEngineTest {

    @Test
    public void runs_Specsy_with_JUnit5() {
        Launcher launcher = LauncherFactory.create();

        SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(summaryListener);
        launcher.registerTestExecutionListeners(new LoggingListener((throwable, messageSupplier) -> {
            System.out.println("[INFO] " + messageSupplier.get());
        }));

        Class<DummySpec> testClass = DummySpec.class;
        launcher.execute(TestDiscoveryRequestBuilder.request()
                .select(forClass(testClass))
                .build());

        TestExecutionSummary summary = summaryListener.getSummary();
        summary.printOn(new PrintWriter(System.out));
        summary.printFailuresOn(new PrintWriter(System.out));
//        ConsoleRunner.main(testClass.getName());

        String summaryString = toString(summary);
        assertThat(summaryString, containsString("4 tests found"));
        assertThat(summaryString, containsString("5 tests started"));
        assertThat(summaryString, containsString("4 tests successful"));
        assertThat(summaryString, containsString("1 tests failed"));
    }

    private static String toString(TestExecutionSummary summary) {
        StringWriter sw = new StringWriter();
        summary.printOn(new PrintWriter(sw));
        return sw.toString();
    }

    public static class DummySpec extends JavaSpecsy {
        @Override
        public void run() throws Throwable {
            spec("passing", () -> {
                spec("nested", () -> {
                });
            });
            spec("failing", () -> {
                throw new AssertionError("dummy failure");
            });
        }
    }
}
