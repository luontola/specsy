package net.orfjackal.specsy.junit

import net.orfjackal.specsy.core.Path
import net.orfjackal.specsy.runner.TestResult
import org.junit.runner.Description

class ResultToDescriptionConverter(testClass: Class[_], results: Map[Path, TestResult]) {
  private val descriptionsByPath = results.mapValues(resultToDescription).toMap
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
    for ((path, desc) <- descriptionsByPath) {
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
