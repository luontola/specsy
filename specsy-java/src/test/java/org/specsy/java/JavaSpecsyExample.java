// Copyright © 2010-2013, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.java;

import org.specsy.GlobalSpy;

public class JavaSpecsyExample extends JavaSpecsy {

    private int counter = 0;

    @Override
    public void run() throws Throwable {

        spec("name of a spec", () -> {
            GlobalSpy.add("spec executed");
        });

        spec("defer blocks", () -> {
            defer(() -> {
                GlobalSpy.add("defer 1"); // happens last
            });
            defer(() -> {
                GlobalSpy.add("defer 2"); // happens first
            });
        });

        spec("isolated", () -> {
            spec("mutation 1", () -> {
                counter++;
            });
            spec("mutation 2", () -> {
                counter++;
            });
            GlobalSpy.add("isolated: " + counter); // expecting 1
        });

        spec("non-isolated", () -> {
            shareSideEffects();
            spec("mutation 1", () -> {
                counter++;
            });
            spec("mutation 2", () -> {
                counter++;
            });
            GlobalSpy.add("non-isolated: " + counter); // expecting 2
        });
    }
}
