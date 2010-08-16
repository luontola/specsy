// Copyright © 2010, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy.examples;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DeferBlocksExampleSpec.class,
        DeferBlocksExample2Spec.class,
        FibonacciSpec.class,
        StackSpec.class
})
public class ExampleSpecs {
}
