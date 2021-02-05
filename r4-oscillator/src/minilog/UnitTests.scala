
package minilog

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  @Test def testOscillatorPeriod2() {
    
    /** The number of times the oscillator is clocked during the test. */
    val clocks = 100
    
    /** Build the oscillator circuit. */
    val circuit = new Circuit()
    val state: Bus = factory.buildOscillatorPeriod2(circuit)
    
    /** Clock the oscillator and check the output (taken from state(0)) 
        after each clocking. The output should be true for 
        t = 0, 2, 4, ... and false otherwise. */
    for (t <- 0 until clocks) {
      val output: Gate = state(0)
      if (t % 2 == 0)
          assertTrue(
              "Test failed with period 2. Output at time %d should be true.".format(t), 
              output.value)
      else
          assertFalse(
              "Test failed with period 2. Output at time %d should be false.".format(t), 
              output.value)
      circuit.clock()
    }
  }

  @Test def testOscillatorPeriod3() {
    
    /** The number of times the oscillator is clocked during the test. */
    val clocks = 100

    /** Build the oscillator circuit. */
    val circuit = new Circuit()
    val state: Bus = factory.buildOscillatorPeriod3(circuit)
 
    /** Clock the oscillator and check the output (taken from state(0)) 
        after each clocking. The output should be true for t = 0, 3, 6, ... 
        and false otherwise. */
    for (t <- 0 until clocks) {
      val output: Gate = state(0)
      if (t % 3 == 0)
          assertTrue(
              "Test failed with period 3. Output at time %d should be true.".format(t), 
              output.value)
        else
          assertFalse(
              "Test failed with period 3. Output at time %d should be false.".format(t), 
              output.value)
        circuit.clock()
    }
  }
  
  @Test def testOscillatorPeriod4() {
    
    /** The number of times the oscillator is clocked during the test. */
    val clocks = 100

    /** Build the oscillator circuit. Note that buildOscillatorPeriod4 returns 
        the output gate instead of the full oscillator state as in the previous
        test cases. */
    val circuit = new Circuit()
    val output: Gate = factory.buildOscillatorPeriod4(circuit)
 
    /** Clock the oscillator and check the output after each clocking. 
        The output should be true for t = 0, 4, 8, ... and false otherwise. */
    for (t <- 0 until clocks) {
      if (t % 4 == 0)
          assertTrue(
              "Test failed with period 4. Output at time %d should be true.".format(t), 
              output.value
              )
        else
          assertFalse(
              "Test failed with period 4. Output at time %d should be false.".format(t), 
              output.value
              )
        circuit.clock()
    }
  }
  
  @Test def testOscillator() {
    
    /** The number of times the oscillator is clocked during the test. */
    val clocks = 100
    
    /** The upper bound for oscillator periods used in the tests. */
    val maxPeriod = 5
    
    /** Test the oscillator with periods from 1 to maxPeriod. */
    for (period <- 1 to maxPeriod) {
      
      /** Build the oscillator circuit. */
      val circuit = new Circuit()
      val output = factory.buildOscillator(circuit, period)
      
      /** Clock the oscillator and check the output after each clocking. 
          The output should be true for t = 0, period, 2*period, ... and 
          false otherwise. */
      for (t <- 0 until clocks) {
        if (t % period == 0)
          assertTrue(
              "Test failed with period %d. Output at time %d should be true.".format(period, t), 
              output.value
              )
        else
          assertFalse(
              "Test failed with period %d. Output at time %d should be false.".format(period, t), 
              output.value
              )
        circuit.clock()
      }
    }
  }
}


