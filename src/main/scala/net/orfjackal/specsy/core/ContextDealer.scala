package net.orfjackal.specsy.core

object ContextDealer {
  private val prepared = new ThreadLocal[Context]

  def prepare(context: Context) {
    prepared.set(context)
  }

  def take(): Context = {
    val context = prepared.get
    assert(context != null, "tried to take the context before it was prepared")
    prepared.set(null)
    context
  }
}
