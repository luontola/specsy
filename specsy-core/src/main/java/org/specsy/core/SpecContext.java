// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core;

import java.util.*;

public class SpecContext {

    private final String name;
    private final SpecContext parent;
    private final Path path;
    private final Path targetPath;

    private Path nextChild;
    private boolean shareSideEffects;
    private final Deque<Closure> deferred = new ArrayDeque<>();

    public SpecContext(String name, SpecContext parent, Path path, Path targetPath) {
        this.name = name;
        this.parent = parent;
        this.path = path;
        this.targetPath = targetPath;
        this.nextChild = path.firstChild();
    }

    public String name() {
        return name;
    }

    public Path path() {
        return path;
    }

    public SpecContext parent() {
        return parent;
    }

    public SpecContext nextChild(String childName) {
        return new SpecContext(childName, this, pathOfNextChild(), targetPath);
    }

    private Path pathOfNextChild() {
        Path path = nextChild;
        nextChild = nextChild.nextSibling();
        return path;
    }


    // sharing side-effects

    public void shareSideEffects() {
        shareSideEffects = true;
    }

    public boolean isShareSideEffects() {
        return shareSideEffects || parent != null && parent.isShareSideEffects();
    }


    // deferring

    public void addDefer(Closure block) {
        deferred.addFirst(block);
    }

    public Iterable<Closure> deferred() {
        return Collections.unmodifiableCollection(deferred);
    }


    // choosing which spec to execute

    public boolean shouldExecute() {
        return isOnTargetPath() || (isUnseen() && isFirstChild()) || isShareSideEffects();
    }

    public boolean shouldPostpone() {
        return isUnseen() && !isFirstChild() && !isShareSideEffects();
    }

    private boolean isOnTargetPath() {
        return path.isOn(targetPath);
    }

    private boolean isUnseen() {
        return path.isBeyond(targetPath);
    }

    private boolean isFirstChild() {
        return path.isFirstChild();
    }
}