// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.specsy

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import net.orfjackal.specsy.core._

class ExecuteOrPostponeTest {
  @Test
  def specs_are_executed_when_on_target_path() {
    val currentPath = Path()
    val targetPath = Path(0)
    shouldExecute(currentPath, targetPath)
  }

  @Test
  def specs_are_executed_when_equal_to_target_path() {
    val currentPath = Path(0)
    val targetPath = Path(0)
    shouldExecute(currentPath, targetPath)
  }

  @Test
  def specs_are_executed_when_first_unseen_child_beyond_target_path() {
    val currentPath = Path(0, 0)
    val targetPath = Path(0)
    shouldExecute(currentPath, targetPath)
  }

  @Test
  def specs_are_postponed_when_other_unseen_child_beyond_target_path() {
    val currentPath = Path(0, 1)
    val targetPath = Path(0)
    shouldPostpone(currentPath, targetPath)
  }

  @Test
  def specs_are_ignored_when_not_on_target_path() {
    val currentPath = Path(1)
    val targetPath = Path(0)
    shouldIgnore(currentPath, targetPath)
  }

  private def shouldExecute(currentPath: Path, targetPath: Path) {
    shouldExecuteOrPostpone(currentPath, targetPath, true, false)
  }

  private def shouldPostpone(currentPath: Path, targetPath: Path) {
    shouldExecuteOrPostpone(currentPath, targetPath, false, true)
  }

  private def shouldIgnore(currentPath: Path, targetPath: Path) {
    shouldExecuteOrPostpone(currentPath, targetPath, false, false)
  }

  private def shouldExecuteOrPostpone(currentPath: Path, targetPath: Path, shouldExecute: Boolean, shouldPostpone: Boolean) {
    val spec = new SpecDeclaration(null, null, currentPath, targetPath)
    assertThat("shouldExecute", spec.shouldExecute, is(shouldExecute))
    assertThat("shouldPostpone", spec.shouldPostpone, is(shouldPostpone))
  }
}
