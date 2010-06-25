package net.orfjackal.specsy.runner

import java.lang._
import scala.collection._
import net.orfjackal.specsy.runner.notification._
import net.orfjackal.specsy.core.Path

class ResultCollector extends TestSuiteNotifier {
  private val testClasses = new mutable.HashMap[Class[_], TestClassState]()

  def visualizedTree: List[String] = {
    val testClassesByName = testClasses.keySet.toList.sortBy(_.getName)
    val results = new mutable.ListBuffer[String]
    for (testClass <- testClassesByName) {
      results.append(testClass.getName)
      results.appendAll(testClasses(testClass).visualizedTree)
    }
    results.toList
  }

  def fireSuiteStarted() {
  }

  def fireSuiteFinished() {
  }

  def newTestClassNotifier(testClass: Class[_]): TestClassNotifier = {
    initMap(testClasses, testClass, new TestClassState(testClass))
  }

  private def initMap[A, B](map: mutable.Map[A, B], key: A, value: B): B = {
    assert(!map.isDefinedAt(key), "was already called for " + key)
    map(key) = value
    value
  }


  private class TestClassState(testClass: Class[_]) extends TestClassNotifier {
    private val runs = new mutable.ArrayBuffer[TestRunState]

    def visualizedTree: List[String] = {
      val tests = mutable.Buffer[TestState]()
      val uniqueTests = new mutable.HashMap[Path, TestState]
      for (run <- runs) {
        uniqueTests ++= run.tests
        tests.appendAll(run.tests.values)
      }
      uniqueTests.values.toList.sortBy(_.path).map(_.nameIndented)
    }

    def fireTestClassStarted() {
    }

    def fireTestClassFinished() {
    }

    def newTestRunNotifier(): TestRunNotifier = {
      val run = new TestRunState
      runs.append(run)
      run
    }
  }

  private class TestRunState extends TestRunNotifier {
    private[ResultCollector] val tests = new mutable.HashMap[Path, TestState]

    def fireTestStarted(path: Path, name: String) {
      initMap(tests, path, new TestState(path, name)).fireTestStarted()
    }

    def fireTestFinished(path: Path) {
      tests(path).fireTestFinished()
    }
  }

  private class TestState(val path: Path, val name: String) {
    def nameIndented: String = {
      val indent = "  " * path.length
      indent + "- " + name
    }

    def fireTestStarted() {
    }

    def fireTestFinished() {
    }
  }
}
