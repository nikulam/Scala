
package minilog

/*
 * Run this object to play with memory units in Trigger.
 */
object play {
  def main(args: Array[String]): Unit = {

    /** Create new Circuit and Trigger instances. */
    val circuit = new Circuit()
    val trigger = new Trigger(circuit)
    
    /** Address and data word lengths in bits. */
    val addressLength = 4
    val dataLength = 16

    /** Create the parameters for the memory units. The address parameter is not 
        needed for the one-word memory unit. */
    val readEnable  = circuit.input()
    val writeEnable = circuit.input()
    val address = circuit.inputs(addressLength)
    val data = circuit.inputs(dataLength)
    
    /** Build the one-word memory unit. */
     //val readOutput = factory.buildOneWordMemory(readEnable, writeEnable, data)
     
    /** Build the multiple-word memory unit. */
     val readOutput = factory.buildMemory(readEnable, writeEnable, address, data)
     println(readOutput.values)
     
    /** Set up Trigger. */
     trigger.watch("readEnable", readEnable)
     trigger.watch("writeEnable", writeEnable)
     trigger.watch("address", address.reverse)   // Used only in a multiple-word memory
     trigger.watch("data", data.reverse)
     trigger.watch("readOutput", readOutput.reverse)    
    
    /** Go Trigger. */
     trigger.go()
  }
}


