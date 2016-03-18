// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.java;

import org.specsy.core.Closure;
import org.specsy.java.JavaSpecsy;

public class EnvironmentFilterExampleSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {
        spec("This test is run every time", () -> {
            // Test code...
        });

        spec("This test is run only under Java 8 and greater", () -> worksOnlyOnJava8(() -> {
            // Test code... For example something which uses the new Date and Time API (JSR-310)
            // which was added in Java 8
        }));

        // This can also be used at the top level, if many/all tests work only on Java 8.
        // Just surround all tests and variable/field declarations into a closure.
        worksOnlyOnJava8(() -> {

            spec("This requires Java 8", () -> {
                // Test code...
            });
            spec("This also requires Java 8", () -> {
                // Test code...
            });
        });
    }

    private static void worksOnlyOnJava8(Closure closure) throws Throwable {
        if (isJava8()) {
            closure.run();
        }
    }

    private static boolean isJava8() {
        try {
            Class.forName("java.time.LocalDate");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
