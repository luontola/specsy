---
title: testing framework for Scala
layout: wikistyle
---

Specsy is a [BDD](http://dannorth.net/introducing-bdd)-style unit-level testing framework for [Scala](http://www.scala-lang.org/). It safely <em>isolates mutable state</em> and supports writing self-documenting tests/specifications.

Specsy has all the <em>essential features</em> of a unit testing framework and nothing excess. To illustrate Specsy's <em>expressiveness</em>, its public API has only three methods, but they provide functionality that requires about four printed pages of documentation - more than Specsy even has production code.

Refer to the [documentation](documentation.html) to see examples of tests written with Specsy.

- Mailing list: <http://groups.google.com/group/specsy>
- Source code: <http://github.com/orfjackal/specsy>
- License: [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
- Developer: [Esko Luontola](http://www.orfjackal.net/)


Project Goals
-------------

- **Unlimited Nesting** - The specs can be organized into a nested hierarchy. This makes it possible to apply [One Assertion Per Test](http://www.artima.com/weblogs/viewpost.jsp?thread=35578) which [isolates](http://agileinaflash.blogspot.com/2009/02/first.html) the reason for a failure, because the specs are very fine-grained. This flexibility also makes writing [specification-style](http://blog.orfjackal.net/2010/02/three-styles-of-naming-tests.html) tests easier.

- **Isolated Execution** - To make it easy to write [repeatable](http://agileinaflash.blogspot.com/2009/02/first.html) tests, each spec is isolated from the side-effects of its sibling specs. By default, each spec will see only the side-effects of its parent specs. Note that Specsy discourages writing non-repeatable fat integration tests, so a [BeforeClass](http://junit.sourceforge.net/javadoc/org/junit/BeforeClass.html)/[AfterClass](http://junit.sourceforge.net/javadoc/org/junit/AfterClass.html) concept is outside the scope of this project (or at a very low priority - I have some ideas on how it could be done elegantly).

- **No Forced Words** - In order to let you choose the best possible test names, Specsy does not impose any [predefined words](http://blog.orfjackal.net/2010/05/choice-of-words-in-testing-frameworks.html) on its users.

- **Simplicity** - Specsy contains only the essential features, but does them well. Having a particular assertion syntax is not essential and it's easy to use the assertions of other testing libraries, so Specsy itself does not have assertions. Also any syntactic sugar is minimized, in order for it to be easy to know what the code does just by looking at it.

- **Parallel Execution** - Running tests [fast](http://agileinaflash.blogspot.com/2009/02/first.html) is a must for using TDD (my pain threshold for recompile and test execution is about 5-10 seconds). Specsy makes it possible to parallelize the test execution using the maximal number of CPU cores (not yet implemented).
