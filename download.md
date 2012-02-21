---
title: Download
layout: page
group: navigation
---
{% include JB/setup %}

You can download Specsy from the [Maven Central Repository](http://repo1.maven.org/maven2/net/orfjackal/specsy/specsy/). The source code is at <https://github.com/orfjackal/specsy>

If you use Maven, add the following dependency to your POM file.

    <dependency>
        <groupId>net.orfjackal.specsy</groupId>
        <artifactId>specsy</artifactId>
        <version>1.2.0</version>
        <scope>test</scope>
    </dependency>

The default artifact is compiled using Scala 2.9.0. If you are using Scala 2.8.1, add the `scala281` classifier to your dependency:

    <dependency>
        <groupId>net.orfjackal.specsy</groupId>
        <artifactId>specsy</artifactId>
        <version>1.2.0</version>
        <classifier>scala281</classifier>
        <scope>test</scope>
    </dependency>

Continue to the [documentation](documentation.html) to find out how to use Specsy.


Version History
---------------

**1.2.0 (2011-05-17)**

- Fixed the order of tests in JUnit results
- Added `shareSideEffects()` for a non-isolated execution model
- Added Scaladocs for the methods in `Spec`
- Compiled with Scala 2.9.0 and 2.8.1

**1.1.0 (2011-05-13)**

- Made the `Spec.defer` method public, to allow it to be used from helper classes
- Renamed the implicit `Spec.specify` method for declaring nested specs, to avoid potential name clashes
- Execute child specs in the same order as they are declared (when single-threaded)
- Made output capturing disabled by default. Use the JVM option `-Dspecsy.captureOutput=true` to enable it
- Upgraded to Scala 2.9.0

**1.0.1 (2010-08-29)**

- Fixed Scala's `println()` not being captured, due to `scala.Console` being unaffected by `java.lang.System.setOut()`

**1.0.0 (2010-08-16)**

- Isolated execution model
- Unlimited nested specs
- Defer blocks
- JUnit test runner


### Known Issues

- The tests are not yet executed in parallel (a new test runner is needed)
- JUnit's test runner API does not support testing frameworks which do not know beforehand what tests there are, but which know it only after executing the tests, so at least IntelliJ IDEA cannot report test progress in real time (a new test runner is needed)
- In IntelliJ IDEA's Run tool window, you should disable "Hide Passed" and enable "Select First Failed Test When Finished". Otherwise IDEA will fail to show the failed tests. This workaround works at least in IDEA 9, but IDEA 10 appears to have some more issues (a new test runner UI is needed)

All of these issues will ultimately be solved when I finish creating [Jumi](http://jumi.fi/), the next *de facto* test runner for the JVM. :)


License
-------

Copyright © 2010-{{ site.time | date: "%Y" }} Esko Luontola <[www.orfjackal.net](http://www.orfjackal.net/)>  
This software is released under the Apache License 2.0.  
The license text is at <http://www.apache.org/licenses/LICENSE-2.0>
