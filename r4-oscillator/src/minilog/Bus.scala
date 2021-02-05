package minilog

import collection.SeqLike
import collection.mutable.{ArrayBuffer,Builder}
import collection.generic.CanBuildFrom

/** A custom collection for bus-level building. */

sealed class Bus(gates: Seq[Gate]) 
             extends Seq[Gate] 
                with SeqLike[Gate,Bus]
{
  /* Relegate to underlying sequence object. */

  def host = gates.head.host
  def length = gates.length
  def apply(idx: Int) = gates.apply(idx)
  def apply(idxs: Seq[Int]) = new Bus(idxs.map(gates(_)))
  def iterator = gates.iterator
  override protected[this] def newBuilder: Builder[Gate, Bus] = Bus.newBuilder

  /** Returns the values of the gates in the bus. */

  def values = gates.map(_.value)

  /** Returns the gate-wise AND of the gates in the left operand with the right operand. */

  def &&(that: Gate) = new Bus(this.map(_ && that))

  /** Returns the gate-wise OR of the gates in the left operand with the right operand. */

  def ||(that: Gate) = new Bus(this.map(_ || that))

  /** Returns the gate-wise XOR of the gates in the left operand with the right operand. */

  def +(that: Gate)  = new Bus(this.map(_ + that))

  /** Returns the NOT of all gates in the operand. */

  def unary_~        = this.map(!_)

  /** Returns the gate-wise AND of its operands. */

  def &(that: Bus)   = new Bus((this zip that).map(x => x._1 && x._2))

  /** Returns the gate-wise OR of its operands. */

  def |(that: Bus)   = new Bus((this zip that).map(x => x._1 || x._2))

  /** Returns the gate-wise XOR of its operands. */

  def ^(that: Bus)   = new Bus((this zip that).map(x => x._1 + x._2))

  /** Builds feedbacks to each gate (input element) in the bus from the corresponding 
    * gate in the operand. */

  def buildFeedback(that: Bus) = (this zip that).foreach(x => x._1.buildFeedback(x._2))
}

/** A companion builder for class Bus. */

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
}
