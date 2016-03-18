// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.groovy

import org.specsy.core.Closure
import org.specsy.groovy.GroovySpecsy

import static org.specsy.examples.groovy.AcceptanceTestHelpers.pendingUntilFixed

class PendingUntilFixedExampleSpec extends GroovySpecsy {
    @Override
    void run() {
        spec "An acceptance test for an already implemented feature", {
            // Test code...
        }

        spec "An acceptance test whose feature has not yet been implemented", {
            pendingUntilFixed {
                // Test code which is still failing...
                throw new AssertionError("this feature is not implemented")
            }
        }
    }
}

class AcceptanceTestHelpers {

    // When this method is in a helper class, it's easy to find all pending tests
    // by searching for all usages of this method with your IDE.
    public static void pendingUntilFixed(Closure closure) {
        try {
            closure.run()
        } catch (Throwable t) {
            System.err.println("This test is pending until fixed:")
            t.printStackTrace()
            return // test is pending
        }
        throw new AssertionError("This test would now pass. Remove the 'pendingUntilFixed' tag.")
    }
}
