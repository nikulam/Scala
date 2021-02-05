
package minilog

import org.junit.Test
import org.junit.Assert._

/**
 * Unit tests for the memory exercise.
 */
class UnitTests {
  
  /** Helper function to set bus values to the given values. */
  def setBusValues(bus: Bus, values: Seq[Boolean]) = {
    require(bus.length == values.length, "Bus length must be equal to the number of given values.")
    (bus zip values).foreach(x => x._1.set(x._2))
  }

  /** Returns a vector with n false values. */
  def falses(n: Int) = (1 to n).map(x => false)
  
  /** Unit test for the one-word memory. */
  @Test def testOneWordMemory() {
    
    /** Create parameters for the one-word memory. 
        The gates have false as the default 
        initial value unless otherwise specified. */
    val dataWord = Seq(true, true, true, false)
    val dataLength = dataWord.length
    val circuit = new Circuit()
    val readEnable = circuit.input()
    val writeEnable = circuit.input()
    val data = circuit.inputs(dataLength)

    /** Build the memory unit. */
    val readOutput = factory.buildOneWordMemory(readEnable, writeEnable, data)
    
    /** Test readEnable = false, writeEnable = false, 
        memory = empty (falses/zeroes). */
    readEnable.set(false)
    writeEnable.set(false)
    assertEquals("The output should be zero if read is not enabled.", 
        falses(dataLength), readOutput.values)
    
    /** Test readEnable = true, writeEnable = false, 
        memory = empty (falses/zeroes). */
    readEnable.set(true)
    assertEquals("The output should be zero if nothing has been written to the memory.", 
        falses(dataLength), readOutput.values)
    
    /** Clock the circuit and test that nothing is written to the memory. */
    setBusValues(data, dataWord)
    circuit.clock()
    assertEquals("The output should be zero if nothing has been written to the memory.", 
        falses(dataLength), readOutput.values)
        
    /** Clock the circuit again with writeEnable = true. Hence, memory = dataWord. */
    writeEnable.set(true)
    circuit.clock()
    assertEquals("The output should correspond to the given input data word.", 
        dataWord, readOutput.values)
        
    /** Test readEnable = false, memory = dataWord. */
    readEnable.set(false)
    assertEquals("The output should be zero if read is not enabled.", 
        falses(dataLength), readOutput.values)
  }
 
  /** Unit test for the multiple-word memory. */
  @Test def testMemory() {
    
    /** Create parameters for the one-word memory. 
        The gates have false as the default 
        initial value unless otherwise specified. */
    val word1 = Seq(true, false, false, false)
    val word2 = Seq(false, true, false, false)
    val word3 = Seq(false, false, true, false)
    val word4 = Seq(false, false, false, true)
    val addressLength = 2
    val wordLength = 4
    val circuit = new Circuit()
    val readEnable = circuit.input()
    val writeEnable = circuit.input()
    val address = circuit.inputs(addressLength)
    val data = circuit.inputs(wordLength)

    /** Build the memory unit. */
    val readOutput = factory.buildMemory(readEnable, writeEnable, address, data)
   
    /** Test readEnable = false, writeEnable = false, memory = empty (falses/zeroes). */
    readEnable.set(false)
    writeEnable.set(false)
    assertEquals("The output should be zero if read is not enabled.", 
        falses(wordLength), readOutput.values)
    
    /** Test readEnable = true, writeEnable = false, 
        memory = empty (falses/zeroes). */
    readEnable.set(true)
    assertEquals("The output should be zero if nothing has been written to the memory.", 
        falses(wordLength), readOutput.values)
    
    /** Set data to the input bus and clock the circuit and test that 
        nothing is written to the memory (writeEnable = false). */
    setBusValues(address, Seq(false, false))
    setBusValues(data, word1)
    circuit.clock()
    assertEquals("The output should be zero if nothing has been written to the memory.", 
        falses(wordLength), readOutput.values)
        
    /** Clock the circuit again so that word1 gets written to memory. Hence, 
        memory = (0, 0, 0, word1). */
    writeEnable.set(true)
    circuit.clock()
    assertEquals("The output should correspond to the data word in address (0, 0).", 
        word1, readOutput.values)

    /** Read from address (1, 0) which should not contain data. */
    setBusValues(address, Seq(true, false))
    assertEquals("Address (1, 0) should not contain any data.", 
        circuit.falses(wordLength).values, readOutput.values)
        
    /** Test readEnable = false. */
    setBusValues(address, Seq(false, false))
    readEnable.set(false)
    assertEquals("The output should be zero if read is not enabled.", 
        falses(wordLength), readOutput.values)
        
    /** Write word2 to memory so that memory = (0, 0, word2, word1) 
        and address = (1, 0). */
    setBusValues(address, Seq(true, false))
    setBusValues(data, word2)
    circuit.clock()
    readEnable.set(true)
    assertEquals("The output should correspond to the data word in address (1, 0).", 
        word2, readOutput.values)
        
    /** Test that word1 is still in memory address (0, 0). */
    setBusValues(address, Seq(false, false))
    assertEquals("The output should correspond to the data word in address (0, 0).", 
        word1, readOutput.values)
  }
}


