// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.java;

import org.specsy.SpecsyContract;

public class JavaSpecsyTest extends SpecsyContract {

    @Override
    public Class<?> testClass() {
        return JavaSpecsyExample.class;
    }
}
