
package tinylog

import collection.SeqLike
import collection.mutable.{ ArrayBuffer, Builder }
import collection.generic.CanBuildFrom

sealed class Bus(gates: Seq[Gate]) extends Seq[Gate] with SeqLike[Gate, Bus] {
  def length = gates.length
  def apply(idx: Int) = gates.apply(idx)
  def apply(idxs: Seq[Int]) = new Bus(idxs.map(gates(_)))
  def iterator = gates.iterator
  override protected[this] def newBuilder: Builder[Gate, Bus] = Bus.newBuilder

  def values = gates.map(_.value)

  /** The number of gates (i) in this bus and (ii) recursively referenced by the ones in this bus. */
  def nofGates: Int = {
    val counted = new scala.collection.mutable.HashSet[Gate]()
    gates.foldLeft(0)((result, gate) => result + gate.nofReferenced(counted))
  }

  /**
   * For a bus aa and gate g, aa && g returns a new bus cc
   * of length aa.length such that cc(i) is aa(i) && g.
   */
  def &&(that: Gate) = new Bus(this.map(_ && that))

  /**
   * For a bus aa and gate g, aa || g returns a new bus cc
   * of length aa.length such that cc(i) is aa(i) || g.
   */
  def ||(that: Gate) = new Bus(this.map(_ || that))

  /**
   * Bitwise negation of the bus.
   * For a bus aa, ~aa is a new bus cc such that cc(i) is !aa(i).
   */
  def unary_~ = this.map(!_)

  /**
   * Bitwise AND of two busses.
   * For two busses aa and bb, aa & bb returns a new bus cc
   * of length aa.length such that cc(i) is aa(i) && bb(i).
   * The busses must be of the same length.
   */
  def &(that: Bus) = {
    require(this.length == that.length, "Cannot take bitwise and of busses of different length")
    new Bus((this zip that).map(x => x._1 && x._2))
  }

  /**
   * Bitwise OR of two busses.
   * For two busses aa and bb, aa | bb returns a new bus cc
   * of length aa.length such that cc(i) is aa(i) || bb(i).
   * The busses must be of the same length.
   */
  def |(that: Bus) = {
    require(this.length == that.length, "Cannot take bitwise and of busses of different length")
    new Bus((this zip that).map(x => x._1 || x._2))
  }
}

object Bus {
  def apply(gates: Gate*) = new Bus(gates)
  def newBuilder: Builder[Gate, Bus] =
    new ArrayBuffer[Gate] mapResult (s => new Bus(s))
  implicit def canBuildFrom: CanBuildFrom[Bus, Gate, Bus] = {
    new CanBuildFrom[Bus, Gate, Bus] {
      def apply(): Builder[Gate, Bus] = newBuilder
      def apply(from: Bus): Builder[Gate, Bus] = newBuilder
    }
  }
  /** Returns a new bus with n InputElement gates */
  def inputs(n: Int) = new Bus((1 to n).map(x => Gate.input()))
  /** Returns a new bus of n False gates */
  def falses(n: Int) = new Bus((1 to n).map(x => Gate.False))
  /** Returns a new bus of n True gates */
  def trues(n: Int) = new Bus((1 to n).map(x => Gate.True))
}


