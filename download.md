---
title: Download
layout: page
group: navigation
---
{% include JB/setup %}

You can download Specsy from the Maven Central Repository under the [org.specsy groupId](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.specsy%22). Specsy's tests are run using the [Jumi test runner](http://jumi.fi/). (The old 1.x versions, which are run with JUnit, use the [net.orfjackal.specsy groupId](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22net.orfjackal.specsy%22).) The source code is at <https://github.com/orfjackal/specsy>

Choose the right artifact based on your language. For example, `specsy-java` is for Java (7 or higher; lambdas recommended), `specsy-groovy` for Groovy (all versions), and `specsy-scala_x.x.x` for a particular Scala version (2.7.7 or higher). Specsy runs on Java 7 or higher (due to [Jumi](http://jumi.fi/) requiring it). It's easy to add support for more JVM-based languages, so create a [feature or pull request](https://github.com/orfjackal/specsy/issues) if you would like your favorite language supported.

Continue to the [documentation](documentation.html) to find out how to use Specsy.


Version History
---------------

**2.1.0 (2012-12-xx)**

- Upgraded to Jumi 0.2.235
- The `@RunVia` annotation is now inherited from the base class, so each test class doesn't need to repeat it. Due to this the `ScalaSpecsy` base class is now an abstract class instead of a trait

**2.0.0 (2012-09-30)**

- Runs using the [Jumi](http://jumi.fi/) test runner, fixing all previously known issues (e.g. it now runs tests in parallel and reports test execution in real time)
- Rewrote the core in Java to support multiple programming languages through thin language-specific frontends
- Supports Scala, version 2.7.7 upwards
- Supports Groovy, all versions
- Supports Java, version 7 upwards (lambdas are recommended for more tolerable syntax noise)

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


License
-------

Copyright © 2010-{{ site.time | date: "%Y" }} Esko Luontola <[www.orfjackal.net](http://www.orfjackal.net/)>  
This software is released under the Apache License 2.0.  
The license text is at <http://www.apache.org/licenses/LICENSE-2.0>
