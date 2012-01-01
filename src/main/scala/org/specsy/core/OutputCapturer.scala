// Copyright © 2010-2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.core

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
