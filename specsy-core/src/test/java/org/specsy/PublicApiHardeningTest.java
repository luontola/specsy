// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.specsy.core.Closure;
import org.specsy.core.Context;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class PublicApiHardeningTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final Context context = new Context(null, null);

    @Test
    public void null_bootstrap_className() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("className"));
        context.bootstrap(null, mock(Closure.class));
    }

    @Test
    public void null_bootstrap_rootSpec() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("rootSpec"));
        context.bootstrap("", null);
    }

    @Test
    public void null_spec_name() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("name"));
        context.spec(null, mock(Closure.class));
    }

    @Test
    public void null_spec_body() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("body"));
        context.spec("", null);
    }

    @Test
    public void null_defer_block() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("block"));
        context.defer(null);
    }
}
