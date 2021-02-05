
package minilog

/*
 * Run this object to play with LFSRs in Trigger.
 *
 */

object play {
  def main(args: Array[String]): Unit = {

    /** Create new Circuit and Trigger instances. */
    val circuit = new Circuit()
    val trigger = new Trigger(circuit)
    
    /** Rotator unit. */
    // val state1 = circuit.inputs(4)
    // val rotator = factory.buildRotator(state1)
    // state1(0).set(true)
    // trigger.watch("Rotator", rotator.reverse)
    
    /** A maximal 5-bit LFSR. */
    // val state2 = circuit.inputs(5)
    // val fiveBitLFSR = factory.build5BitLFSR(state2)
    // state2(0).set(true)
    // trigger.watch("5-bit LFSR", fiveBitLFSR.reverse)
    
    /** LFSR unit. */
    // val state3 = circuit.inputs(4)
    // val taps = List(1)
    // val LFSR = factory.buildLFSR(taps, state3)
    // state3(0).set(true)
    // trigger.watch("LFSR", LFSR.reverse)
 	
    /** Go Trigger. */
    // trigger.go()
  }
}

