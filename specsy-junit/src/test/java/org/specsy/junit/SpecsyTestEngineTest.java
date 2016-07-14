// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import org.junit.Test;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.LoggingListener;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.*;
import static org.junit.platform.launcher.EngineFilter.includeEngines;

public class SpecsyTestEngineTest {

    @Test
    public void runs_Specsy_with_JUnit() {
        Launcher launcher = LauncherFactory.create();

        SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(summaryListener);
        launcher.registerTestExecutionListeners(new LoggingListener((throwable, messageSupplier) -> {
            System.out.println("[INFO] " + messageSupplier.get());
        }));

        launcher.execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(SampleSpec.class))
                .build());

        TestExecutionSummary summary = summaryListener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        summary.printFailuresTo(new PrintWriter(System.out));
//        ConsoleLauncher.main(SampleSpec.class.getName());

        String summaryString = toString(summary);
        assertThat(summaryString, containsString("3 tests found"));
        assertThat(summaryString, containsString("3 tests started"));
        assertThat(summaryString, containsString("2 tests successful"));
        assertThat(summaryString, containsString("1 tests failed"));
    }

    @Test
    public void reported_UniqueIds() {
        List<TestIdentifier> tests = runTests(selectClass(SampleSpec.class));

        assertThat("tests", tests, hasSize(6));
        assertThat(tests.get(0).getUniqueId(), is("[engine:specsy]"));
        assertThat(tests.get(1).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]"));
        assertThat(tests.get(2).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]/[nested:0]"));
        assertThat(tests.get(3).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]/[nested:0]/[nested:0]"));
        assertThat(tests.get(4).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]"));
        assertThat(tests.get(5).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]/[nested:1]"));
    }

    @Test
    public void reported_display_names() {
        List<TestIdentifier> tests = runTests(selectClass(SampleSpec.class));

        assertThat(tests.get(0).getDisplayName(), is("Specsy"));
        assertThat(tests.get(1).getDisplayName(), is("SampleSpec"));
        assertThat(tests.get(2).getDisplayName(), is("passing"));
        assertThat(tests.get(3).getDisplayName(), is("nested"));
        assertThat(tests.get(5).getDisplayName(), is("failing"));
    }


    // supported discovery selectors

    @Test
    public void select_tests_by_class() {
        List<TestIdentifier> tests = runTests(selectClass(SampleSpec.class));

        assertThat("tests", tests, hasSize(6));
        assertThat(tests.get(0).getUniqueId(), is("[engine:specsy]"));
        assertThat(tests.get(1).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]"));
    }

    @Test
    public void select_tests_by_UniqueId() {
        List<TestIdentifier> tests = runTests(selectUniqueId("[engine:specsy]/[class:org.specsy.junit.SampleSpec]/[nested:0]"));

        assertThat("tests", tests, hasSize(4));
        assertThat(tests.get(0).getUniqueId(), is("[engine:specsy]"));
        assertThat(tests.get(1).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]"));
        assertThat(tests.get(2).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]/[nested:0]"));
        assertThat(tests.get(3).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]/[nested:0]/[nested:0]"));
    }

    @Test
    public void select_tests_by_classpath() {
        List<TestIdentifier> tests = runTests(selectClasspathRoots(myClasspathRoot()));

        assertThat("tests", tests, hasSize(6));
        assertThat(tests.get(0).getUniqueId(), is("[engine:specsy]"));
        assertThat(tests.get(1).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]"));
    }

    @Test
    public void select_tests_by_package() {
        List<TestIdentifier> tests = runTests(selectPackage(SampleSpec.class.getPackage().getName()));

        assertThat("tests", tests, hasSize(6));
        assertThat(tests.get(0).getUniqueId(), is("[engine:specsy]"));
        assertThat(tests.get(1).getUniqueId(), is("[engine:specsy]/[class:org.specsy.junit.SampleSpec]"));
    }


    // helpers

    private static List<TestIdentifier> runTests(DiscoverySelector selector) {
        return runTests(Collections.singletonList(selector));
    }

    private static List<TestIdentifier> runTests(List<DiscoverySelector> selectors) {
        Launcher launcher = LauncherFactory.create();
        List<TestIdentifier> tests = new ArrayList<>();
        launcher.registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void executionStarted(TestIdentifier testIdentifier) {
                tests.add(testIdentifier);
            }
        });
        launcher.execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectors)
                .filters(includeEngines("specsy")) // avoid infinite recursion due to accidentally running SpecsyTestEngineTest
                .build());
        return tests;
    }

    private static String toString(TestExecutionSummary summary) {
        StringWriter sw = new StringWriter();
        summary.printTo(new PrintWriter(sw));
        return sw.toString();
    }

    private Set<File> myClasspathRoot() {
        String classFile = getClass().getName().replace(".", "/") + ".class";
        URL resource = getClass().getResource("/" + classFile);
        File file = new File(resource.getPath().replace(classFile, ""));
        return Collections.singleton(file);
    }
}
