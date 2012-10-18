// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.java;

import fi.jumi.api.RunVia;
import org.specsy.*;
import org.specsy.core.Closure;

@RunVia(Specsy.class)
public class JavaSpecsyExample extends JavaSpecsy {

    private int counter = 0;

    @Override
    public void run() throws Throwable {

        spec("name of a spec", new Closure() {
            @Override
            public void run() throws Throwable {
                GlobalSpy.add("spec executed");
            }
        });

        spec("defer blocks", new Closure() {
            @Override
            public void run() throws Throwable {
                defer(new Closure() {
                    @Override
                    public void run() throws Throwable {
                        GlobalSpy.add("defer 1"); // happens last
                    }
                });
                defer(new Closure() {
                    @Override
                    public void run() throws Throwable {
                        GlobalSpy.add("defer 2"); // happens first
                    }
                });
            }
        });

        spec("isolated", new Closure() {
            @Override
            public void run() throws Throwable {
                spec("mutation 1", new Closure() {
                    @Override
                    public void run() throws Throwable {
                        counter++;
                    }
                });
                spec("mutation 2", new Closure() {
                    @Override
                    public void run() throws Throwable {
                        counter++;
                    }
                });
                GlobalSpy.add("isolated: " + counter); // expecting 1
            }
        });

        spec("non-isolated", new Closure() {
            @Override
            public void run() throws Throwable {
                shareSideEffects();
                spec("mutation 1", new Closure() {
                    @Override
                    public void run() throws Throwable {
                        counter++;
                    }
                });
                spec("mutation 2", new Closure() {
                    @Override
                    public void run() throws Throwable {
                        counter++;
                    }
                });
                GlobalSpy.add("non-isolated: " + counter); // expecting 2
            }
        });
    }
}
