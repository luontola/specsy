package net.orfjackal.specsy.core

import java.io._

class OutputCapturer(realOut: PrintStream, realErr: PrintStream) {
  private var capture: Capture = null

  val capturedOut = new PrintStream(new OutputStream {
    def write(b: Int) {
      if (capture != null) {
        capture.write(b)
      } else {
        realOut.write(b)
      }
    }
  })
  val capturedErr = new PrintStream(new OutputStream {
    def write(b: Int) {
      if (capture != null) {
        capture.write(b)
      } else {
        realErr.write(b)
      }
    }
  })

  def beginCapture(): Capture = {
    capture = new Capture
    capture
  }

  def stop() { // TODO: move to the Capture class? allow multiple concurrent captures?
    capture = null
  }
}

class Capture extends ByteArrayOutputStream {
}
