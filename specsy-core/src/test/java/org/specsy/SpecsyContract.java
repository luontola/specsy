// Copyright © 2010-2013, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy;

import fi.jumi.api.drivers.TestId;
import fi.jumi.core.api.TestFile;
import fi.jumi.core.results.SuiteEventDemuxer;
import fi.jumi.core.testbench.TestBench;
import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class SpecsyContract {

    private SuiteEventDemuxer results;

    @Before
    public void resetSpy() {
        GlobalSpy.reset();
        results = new TestBench().run(testClass());
    }

    public abstract Class<?> testClass();

    @Test
    public void executes_specs() {
        GlobalSpy.assertContains("spec executed");
    }

    @Test
    public void reports_spec_names() {
        String name = results.getTestName(TestFile.fromClass(testClass()), TestId.of(0));
        assertThat(name, is("name of a spec"));
    }

    @Test
    public void executes_defer_blocks_in_reverse_order() {
        GlobalSpy.assertContainsInPartialOrder("defer 2", "defer 1");
    }

    @Test
    public void can_use_isolated_execution_mode() {
        GlobalSpy.assertContains("isolated: 1");
    }

    @Test
    public void can_use_non_isolated_execution_mode() {
        GlobalSpy.assertContains("non-isolated: 2");
    }
}
