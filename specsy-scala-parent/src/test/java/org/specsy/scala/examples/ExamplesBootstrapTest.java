// Copyright © 2010-2013, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.scala.examples;

import fi.jumi.launcher.JumiBootstrap;
import org.junit.Test;

public class ExamplesBootstrapTest {

    // TODO: remove this boostrap when Jumi can be run without it

    private static final long TIMEOUT = 15 * 1000;

    @Test(timeout = TIMEOUT)
    public void run_using_Jumi() throws Exception {
        new JumiBootstrap()
                //.enableDebugMode()
                .setPassingTestsVisible(true)
                .runTestsMatching("glob:org/specsy/scala/examples/**Spec.class");
    }
}
