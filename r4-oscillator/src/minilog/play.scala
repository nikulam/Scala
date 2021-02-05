
package minilog

/*
 * Run this object to play with oscillators in Trigger.
 */
object play {
  def main(args: Array[String]): Unit = {

    /** Create new Circuit and Trigger instances. */
    val circuit = new Circuit()
    val trigger = new Trigger(circuit)
    
    /** Oscillator with period 2 (full circuit state is shown). */
    val stateP2: Bus = factory.buildOscillatorPeriod2(circuit)
    trigger.watch("State of an oscillator with period 2", stateP2.reverse)
    
    /** Oscillator with period 3 (full circuit state is shown). */
    val stateP3: Bus = factory.buildOscillatorPeriod3(circuit)
    trigger.watch("State of an oscillator with period 3", stateP3.reverse)
    
    /** Oscillator with period 4 (only the output is shown). */
    // val outputP4: Gate = factory.buildOscillatorPeriod4(circuit)
    // trigger.watch("Output of an oscillator with period 4", outputP4)

    /** Oscillator with a user-defined period (only the output is shown). */
    // val period = 3
    // val output: Gate = factory.buildOscillator(circuit, period)
    // trigger.watch("Output of an oscillator with period %d".format(period), output)
 	
    /** Go Trigger. */
    trigger.go()
  }
}


