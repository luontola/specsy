// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.java;

import org.specsy.java.JavaSpecsy;

public class HelloWorldSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {

        // top-level spec; add your test code here and/or the child specs

        spec("name of the spec", () -> {
            // first child spec
        });

        spec("name of the spec", () -> {
            // second child spec

            spec("name of the spec", () -> {
                // a nested child spec
            });
        });
    }
}
