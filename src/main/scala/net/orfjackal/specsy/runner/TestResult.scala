package net.orfjackal.specsy.runner

import net.orfjackal.specsy.core.Path

trait TestResult {
  def path: Path

  def name: String

  def location: Object

  def failures: List[Throwable]

  def output: String
}
