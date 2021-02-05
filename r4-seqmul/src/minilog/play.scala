
package minilog

/*
 * Run this object to play with a sequential multiplier in Trigger.
 *
 */
object play {
  def main(args: Array[String]): Unit = {

    /** Create new Circuit and Trigger instances. */
    val circuit = new Circuit()
    val trigger = new Trigger(circuit)
        
    /* Uncomment lines to run the sequential multiplier. */

    /** Build the sequential multiplier. */
     val n = 8
     val input1 = circuit.inputs(n)
     val input2 = circuit.inputs(n)
     val loadEnable = circuit.input()
     val (ready, result) = 
       factory.buildSequentialMultiplier(input1, input2, loadEnable)
    
    /** Set up Trigger and go. */
     trigger.watch("Input 1", input1.reverse)
     trigger.watch("Input 2", input2.reverse)
     trigger.watch("Load enable", loadEnable)
     trigger.watch("Ready", ready)
     trigger.watch("Result", result.reverse)
     trigger.go()
  }
}


