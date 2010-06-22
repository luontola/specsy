package net.orfjackal.specsy

object ContextDealer {
  private val prepared = new ThreadLocal[Context]

  def prepare(context: Context) {
    prepared.set(context)
  }

  def take(): Context = {
    val context = prepared.get
    assert(context != null, "Tried to take the Context before it was prepared")
    prepared.set(null)
    context
  }
}
