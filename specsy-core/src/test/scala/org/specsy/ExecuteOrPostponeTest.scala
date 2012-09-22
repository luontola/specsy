// Copyright © 2010-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers._
import org.specsy.core._

class ExecuteOrPostponeTest {

  @Test
  def specs_are_executed_when_on_target_path() {
    val currentPath = Path.of()
    val targetPath = Path.of(0)
    shouldExecute(currentPath, targetPath)
  }

  @Test
  def specs_are_executed_when_equal_to_target_path() {
    val currentPath = Path.of(0)
    val targetPath = Path.of(0)
    shouldExecute(currentPath, targetPath)
  }

  @Test
  def specs_are_executed_when_first_unseen_child_beyond_target_path() {
    val currentPath = Path.of(0, 0)
    val targetPath = Path.of(0)
    shouldExecute(currentPath, targetPath)
  }

  @Test
  def specs_are_postponed_when_other_unseen_child_beyond_target_path() {
    val currentPath = Path.of(0, 1)
    val targetPath = Path.of(0)
    shouldPostpone(currentPath, targetPath)
  }

  @Test
  def specs_are_ignored_when_not_on_target_path() {
    val currentPath = Path.of(1)
    val targetPath = Path.of(0)
    shouldIgnore(currentPath, targetPath)
  }

  private def shouldExecute(currentPath: Path, targetPath: Path) {
    shouldExecuteOrPostpone(currentPath, targetPath, shouldExecute = true, shouldPostpone = false)
  }

  private def shouldPostpone(currentPath: Path, targetPath: Path) {
    shouldExecuteOrPostpone(currentPath, targetPath, shouldExecute = false, shouldPostpone = true)
  }

  private def shouldIgnore(currentPath: Path, targetPath: Path) {
    shouldExecuteOrPostpone(currentPath, targetPath, shouldExecute = false, shouldPostpone = false)
  }

  private def shouldExecuteOrPostpone(currentPath: Path, targetPath: Path, shouldExecute: Boolean, shouldPostpone: Boolean) {
    val spec = new SpecContext(null, null, currentPath, targetPath)
    assertThat("shouldExecute", spec.shouldExecute, is(shouldExecute))
    assertThat("shouldPostpone", spec.shouldPostpone, is(shouldPostpone))
  }
}
