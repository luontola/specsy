// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core;

import fi.jumi.api.drivers.TestId;

public class Path {

    public static final Path ROOT = new Path(TestId.ROOT);

    private final TestId id;

    private Path(TestId id) {
        this.id = id;
    }

    public static Path of(int... indexes) {
        return new Path(TestId.of(indexes));
    }

    public boolean isFirstChild() {
        return id.isFirstChild();
    }

    public Path firstChild() {
        return new Path(id.getFirstChild());
    }

    public Path nextSibling() {
        return new Path(id.getNextSibling());
    }

    public boolean isOn(Path that) {
        return this.id.equals(that.id) || this.id.isAncestorOf(that.id);
    }

    public boolean isBeyond(Path that) {
        return this.id.isDescendantOf(that.id);
    }

    public TestId toTestId() {
        return id;
    }

    @Override
    public String toString() {
        return "Path(" + id + ")";
    }
}
