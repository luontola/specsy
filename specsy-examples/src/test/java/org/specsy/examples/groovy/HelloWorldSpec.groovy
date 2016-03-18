// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.groovy

import org.specsy.groovy.GroovySpecsy

class HelloWorldSpec extends GroovySpecsy {
    @Override
    void run() {

        // top-level spec; add your test code here and/or the child specs

        spec "name of the spec", {
            // first child spec
        }

        spec "name of the spec", {
            // second child spec

            spec "name of the spec", {
                // a nested child spec
            }
        }
    }
}
