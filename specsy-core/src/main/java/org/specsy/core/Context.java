// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core;

import fi.jumi.api.drivers.*;

import java.util.*;

import static org.specsy.core.Context.Status.*;

public class Context {

    enum Status {
        NOT_STARTED,
        RUNNING,
        FINISHED
    }

    private final Path targetPath;
    private final SuiteNotifier notifier;

    private Status status = NOT_STARTED;
    private SpecContext current = null;
    private final List<Path> postponed = new ArrayList<>();

    public Context(Path targetPath, SuiteNotifier notifier) {
        this.targetPath = targetPath;
        this.notifier = notifier;
    }


    // control flow

    public void bootstrap(String className, Closure rootSpec) {
        changeStatus(NOT_STARTED, RUNNING);

        enterRootSpec(className);
        processSpec(rootSpec);
        exitSpec();

        changeStatus(RUNNING, FINISHED);
    }

    private void enterRootSpec(String name) {
        current = new SpecContext(name, null, Path.ROOT, targetPath);
    }

    public void spec(String name, Closure spec) {
        assertStatusIs(RUNNING);

        enterSpec(name);
        processSpec(spec);
        exitSpec();
    }

    private void enterSpec(String name) {
        current = current.nextChild(name);
    }

    private void processSpec(Closure spec) {
        if (current.shouldExecute()) {
            execute(spec);
        }
        if (current.shouldPostpone()) {
            postponed.add(current.path());
        }
    }

    private void exitSpec() {
        current = current.parent();
    }


    // executing

    private void execute(Closure spec) {
        notifier.fireTestFound(current.path().toTestId(), current.name());
        TestNotifier tn = notifier.fireTestStarted(current.path().toTestId());

        executeSafely(spec, tn);
        for (Closure deferBlock : current.deferred()) {
            executeSafely(deferBlock, tn);
        }

        tn.fireTestFinished();
    }

    private void executeSafely(Closure closure, TestNotifier tn) {
        try {
            closure.run();
        } catch (Throwable t) {
            tn.fireFailure(t);
        }
    }


    // deferring

    public void defer(Closure block) {
        current.addDefer(block);
    }

    public Iterable<Path> postponedPaths() {
        assertStatusIs(FINISHED);
        return Collections.unmodifiableCollection(postponed);
    }


    // sharing side-effects

    public void shareSideEffects() {
        current.shareSideEffects();
    }


    // status

    private void changeStatus(Status from, Status to) {
        assertStatusIs(from);
        status = to;
    }

    private void assertStatusIs(Status expected) {
        if (status != expected) {
            throw new IllegalStateException("expected status " + expected + ", but was " + status);
        }
    }
}
