// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.junit

import org.specsy.core.Path
import org.specsy.runner.TestResult
import org.junit.runner.Description
import scala.collection._

class ResultToDescriptionConverter(testClass: Class[_], results: Map[Path, TestResult]) {
  // the map must be strict, because we will modify the instances it contains
  private val descriptionsByPath = mutable.Map() ++ results.mapValues(resultToDescription)
  addChildrenToParents(descriptionsByPath)

  private def resultToDescription(result: TestResult) = {
    if (isRootSpec(result)) {
      Description.createSuiteDescription(testClass)
    } else if (isLeafSpec(result)) {
      Description.createTestDescription(testClass, result.name)
    } else {
      Description.createSuiteDescription(result.name)
    }
  }

  private def isRootSpec(spec: TestResult): Boolean = {
    spec.path.isRoot
  }

  private def isLeafSpec(spec: TestResult): Boolean = {
    val childrenOfSpec = results.keys.filter(_.parent == spec.path)
    childrenOfSpec.isEmpty
  }

  private def addChildrenToParents(descriptionsByPath: Map[Path, Description]) {
    for ((path, desc) <- descriptionsByPath.toList.sortBy(_._1)) {
      if (!path.isRoot) {
        val parentDesc = descriptionsByPath(path.parent)
        parentDesc.addChild(desc)
      }
    }
  }

  def pathsInOrder: List[Path] = results.keys.toList.sorted

  def topLevelDescription: Description = descriptionForPath(Path())

  def descriptionForPath(path: Path): Description = descriptionsByPath(path)

  def resultForPath(path: Path): TestResult = results(path)
}
