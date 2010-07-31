package net.orfjackal.specsy.core

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import java.io._

class OutputCapturerTest {
  val realOut = new ByteArrayOutputStream
  val realErr = new ByteArrayOutputStream
  val capturer = new OutputCapturer(new PrintStream(realOut), new PrintStream(realErr))
  val out = capturer.capturedOut
  val err = capturer.capturedErr

  @Test
  def captures_what_is_printed_during_capturing() {
    val capture = capturer.beginCapture()
    out.print("printed during capturing")
    capturer.stop()

    assertThat(capture.toString, is("printed during capturing"))
  }

  @Test
  def stdout_and_stderr_are_captured_merged_together() {
    val capture = capturer.beginCapture()
    out.print("1")
    err.print("2")
    out.print("3")
    capturer.stop()

    assertThat(capture.toString, is("123"))
  }

  @Test
  def does_not_capture_what_is_printed_before_or_after_capturing() {
    out.print("before")
    val capture = capturer.beginCapture()
    capturer.stop()
    out.print("after")

    assertThat(capture.toString, is(""))
  }

  @Test
  def what_is_captured_is_NOT_printed_on_screen() {
    val capture = capturer.beginCapture()
    out.print("out-captured")
    err.print("err-captured")
    capturer.stop()

    assertThat(realOut.toString, is(""))
    assertThat(realErr.toString, is(""))
  }

  @Test
  def what_is_NOT_captured_is_printed_on_screen() {
    out.print("out-before ")
    err.print("err-before ")
    val capture = capturer.beginCapture()
    capturer.stop()
    out.print("out-after")
    err.print("err-after")

    assertThat(realOut.toString, is("out-before out-after"))
    assertThat(realErr.toString, is("err-before err-after"))
  }

  @Test
  def captures_what_is_printed_in_spawned_threads() {
    val capture = capturer.beginCapture()

    val t = new Thread(new Runnable {
      def run() {
        out.print("printed in spawned thread")
      }
    })
    t.start()
    t.join()
    capturer.stop()

    assertThat(capture.toString, is("printed in spawned thread"))
  }

  // TODO: when multi threading is added to SuiteRunner, or wait for CTR4J: does_not_capture_what_is_printed_in_unrelated_threads
}
