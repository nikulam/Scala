package tinylog

/** The abstract base class for all our Boolean gate types */
sealed abstract class Gate() {
  def value: Boolean
  def unary_!(): Gate = new NotGate(this)
  def &&(that: Gate): Gate = new AndGate(this, that)
  def ||(that: Gate): Gate = new OrGate(this, that)
  def depth: Int
  /**
   * The number of gates recursively referenced by this gate (including the gate itself) that are
   * not already in the set "counted".
   * The set "counted" is updated while evaluating the result.
   */
  def nofReferenced(implicit counted: scala.collection.mutable.Set[Gate] = new scala.collection.mutable.HashSet[Gate]()): Int
}

/**
 * Companion object allowing easier construction of constant and input gates
 */
object Gate {
  val False: Gate = new ConstantGate(false)
  val True: Gate = new ConstantGate(true)
  def input() = new InputElement()
}

sealed class InputElement() extends Gate() {
  var v = false // default value is false
  def set(s: Boolean) = { v = s }
  def value = v
  def depth = ???
  def nofReferenced(implicit counted: scala.collection.mutable.Set[Gate]) = if (counted.add(this)) 1 else 0
}

sealed class NotGate(in: Gate) extends Gate() {
  def value = !in.value
  def depth = ???
  def nofReferenced(implicit counted: scala.collection.mutable.Set[Gate]) = if (counted.add(this)) in.nofReferenced(counted) + 1 else 0
}

sealed class OrGate(in1: Gate, in2: Gate) extends Gate() {
  def value = in1.value || in2.value
  def depth = ???
  def nofReferenced(implicit counted: scala.collection.mutable.Set[Gate]) =
    if (counted.add(this)) in1.nofReferenced(counted) + in2.nofReferenced(counted) + 1 else 0
}

sealed class AndGate(in1: Gate, in2: Gate) extends Gate() {
  def value = in1.value && in2.value
  def depth = ???
  def nofReferenced(implicit counted: scala.collection.mutable.Set[Gate]) =
    if (counted.add(this)) in1.nofReferenced(counted) + in2.nofReferenced(counted) + 1 else 0
}

sealed class ConstantGate(v: Boolean) extends Gate() {
  def value = v
  def depth = ???
  def nofReferenced(implicit counted: scala.collection.mutable.Set[Gate]) = if (counted.add(this)) 1 else 0
}

