// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit;

import org.specsy.java.JavaSpecsy;

/**
 * Guinea pig used by {@link SpecsyTestEngineTest}. Do not change.
 */
public class SampleSpec extends JavaSpecsy {
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
