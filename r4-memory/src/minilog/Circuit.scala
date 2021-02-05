package minilog

import collection.immutable.Queue

/** A host class for gates with factory methods for input elements and constant gates. */

sealed class Circuit() {

  /* Hosting mechanisms for gates and input elements (internal to package). */

  /** Constructed gates register here. */

  private var gates  = Queue[Gate]()

  /** Returns the size of the circuit, i.e., the number of gates. */
  
  def numberOfGates(): Int = gates.size 

  /** Registers a gate with its host. */

  private[minilog] def registerGate(g: Gate) { 
    gates = gates :+ g
    dirty = true 
  }

  /** Constructed inputs register here. */

  private var ins = Queue[InputElement]()  

  /** Registers an input element with its host. */

  private[minilog] def registerInput(g: InputElement) { ins = ins :+ g }

  /* Memoization and clean/dirty interface 
   * (internal to classes Gate and Circuit). */

  /** Flag: must recompute the memorized values (if any)? */

  private[minilog] var dirty = false

  /** Recomputes the memorized gate values. */ 

  private[minilog] def clean() { 
    dirty = false            // clear dirty before eval, otherwise infinite loop
    gates.foreach(_.clean()) // update and memorize values at gates
  }

  /** Circuit depth. */
 
  def depth = { if(gates.isEmpty) 0 else gates.map(_.depth).max }

  /* Interface for feedback hooks. */

  /** Feedback hooks register here. */ 

  private var hooks = Queue[() => (() => Unit)]()

  /** Builds a feedback hook. */

  def buildFeedbackHook(hook: () =>  (() => Unit)) { hooks = hooks :+ hook } 

  /** Executes feedback. */

  def clock() {
    val writehooks = hooks.map(_()) // run read hooks
    (ins zip ins.map(_.feedbackValue)).foreach({ case (w,v) => w.set(v) })
    writehooks.foreach(_())         // run write hooks
  }

  /* Static objects and builders. */

  /** A static gate that evaluates to false. */

  val False: Gate = new ConstantGate(this, false)

  /** A static gate that evaluates to true. */

  val True: Gate  = new ConstantGate(this, true)

  /** Returns a new input element. */

  def input()        = new InputElement(this)

  /** Returns a bus of n new input elements. */

  def inputs(n: Int) = new Bus((1 to n).map(x => input()))

  /** Returns a new bus of n constant gates that evaluate to false. */

  def falses(n: Int) = new Bus((1 to n).map(x => False))

  /** Returns a new bus of n constant gates that evaluate to true. */

  def trues(n: Int)  = new Bus((1 to n).map(x => True))
}
