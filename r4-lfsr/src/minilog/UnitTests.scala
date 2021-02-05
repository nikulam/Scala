
package minilog

import org.junit.Test
import org.junit.Assert._

/**
 * Unit tests for the LFSR exercise. We use 1 and 0 to denote the gate values 
 * true and false, respectively.
 */
class UnitTests {
  
  /** Unit test for the rotator unit. */
  @Test def testRotator() {
    
    /** The number of times the circuit is clocked during the test. */
    val clocks = 100
    
    /** Create the circuit. */
    val circuit = new Circuit()

    /** Test the circuit using the initialization state (0, 1, 1). 
        After 100 clocks, the state of the rotator should be (1, 0, 1), 
        where the rightmost value represents the value of gate 0. */
    var n = 3
    var state = circuit.inputs(n)
    var rotator = factory.buildRotator(state)
    state(0).set(true)
    state(1).set(true)
    for (t <- 0 until clocks) circuit.clock()
    assertEquals("The rotator should have the state (1, 0, 1) " +
    	"after 100 clocks when the initial state (0, 1, 1) is used", 
        Seq(true, false, true), rotator.values.reverse)
    
    /** Perform another test using the initial state (0, 1, 1, 0, 1). */
    n = 5
    state = circuit.inputs(n)
    rotator = factory.buildRotator(state)
    state(0).set(true)
    state(2).set(true)
    state(3).set(true)
    for (t <- 0 until clocks) circuit.clock()
    assertEquals("The rotator should have the state (0, 1, 1, 0, 1) " +
    	"after 100 clocks when the init state (0, 1, 1, 0, 1) is used", 
        Seq(false, true, true, false, true), rotator.values.reverse)
  
    /** Perform another test using the initial state (0, 0, 0, 1, 1, 1). */
    n = 6
    state = circuit.inputs(n)
    rotator = factory.buildRotator(state)
    state(0).set(true)
    state(1).set(true)
    state(2).set(true)
    for (t <- 0 until clocks) circuit.clock()
    assertEquals("The rotator should have the state (0, 1, 1, 1, 0, 0) " +
    	"after 100 clocks when the init state (0, 0, 0, 1, 1, 1) is used", 
        Seq(false, true, true, true, false, false), rotator.values.reverse)
  }
  
  /** Unit test for the LFSR with 5 gates. */
  @Test def test5BitLFSR() {
    
    /** The number of times the circuit is clocked during the test. This  
        LFSR cycles through 31 distinct states from a non-zero initial state. 
        There are 31 distinct non-zero states, since there are 2^5-1 = 31 
        non-zero 5-bit numbers. */
    val clocks = 31
    
    /** If the LFSR is initialized with a state that has value true in gate 0 
        and false in other gates, and if the outputs are taken from gate 0 at 
        each time instance, the following sequence should be obtained. The 
        sequence has period 31 since the LFSR has 31 distinct states. */
    val refOutputs = 
      Seq(true, false, false, false, false, 
          true, false, false, true, false, 
          true, true, false, false, true, 
          true, true, true, true, false, 
          false, false, true, true, false, 
          true, true, true, false, true, 
          false)
    
    /** Build the LFSR and check that the output of the LFSR, i.e., gate 0, 
        has the correct value for times t = 0, 1, ..., 30. The values are 
        checked using the reference outputs. */
    val circuit = new Circuit()
    val state = circuit.inputs(5)
    val LFSR = factory.build5BitLFSR(state)
    state(0).set(true)
    for (t <- 0 until clocks) { 
      assertEquals("The output at time %d should be %b.".format(t, refOutputs(t)), 
          refOutputs(t), LFSR(0).value)
      circuit.clock()
    }
  }
  
  /** Unit test for the LFSR unit. */
  @Test def testLFSR() {
    
    /** The number of times the circuit is clocked during the test. */
    val clocks = 100
    
    /** Create a new circuit instance. */
    val circuit = new Circuit()
          
    /** Build a maximal LFSR with length 4 using the tap set {1} and initial 
        state (0, 0, 0, 1). */
    val n = 4    
    val state = circuit.inputs(n)
    val taps = List(1)
    val LFSR = factory.buildLFSR(taps, state)
    state(0).set(true)
    
    /** The following output sequence with period 15 should be obtained from the 
        LFSR in this case. */
    val period = 15
    val refOutputs = 
      Seq(true, false, false, false, 
          true, false, false, true, 
          true, false, true, false, 
          true, true, true)
    
    /** Check the values using the reference outputs. */
    for (t <- 0 until clocks) { 
      assertEquals("The output at time %d should be %b.".format(t, refOutputs(t % period)), 
          refOutputs(t % period), LFSR(0).value)
      circuit.clock()
    }
  }
}

