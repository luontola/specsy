// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.bootstrap;

import org.specsy.core.Context;

public class ContextDealer {

    private static final ThreadLocal<Context> prepared = new ThreadLocal<>();

    public static void prepare(Context context) {
        prepared.set(context);
    }

    public static Context take() {
        Context context = prepared.get();
        if (context == null) {
            throw new IllegalStateException("tried to take the context before it was prepared");
        }
        prepared.remove();
        return context;
    }
}
