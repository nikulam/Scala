
package minilog

import org.junit.Test
import org.junit.Assert._

/**
 * Unit tests for the sequential multiplier exercise.
 */
class UnitTests {
  
  /** Helper function to set bus values to the given values. */
  def setBusValues(bus: Bus, values: Seq[Boolean]) = {
    require(bus.length == values.length, 
        "Bus length must be equal to the number of given values.")
    (bus zip values).foreach(x => x._1.set(x._2))
  }

  /** Returns the Boolean vector representation of an integer. 
      The returned array has exactly the number of boolean values indicated 
      by 'size'. Too large input values are truncated. */
  def booleanOf(value: Int, size: Int) = {
    require(value >= 0, "Given value must be non-negative.")
    require(size > 0, "Given size must be positive.")
    (0 until size).map(j => ((value >> j)&1) == 1)
  }
  
  /** Returns a vector with n false values. */
  def falses(n: Int) = (1 to n).map(x => false)
  
  /** Unit test for the sequential multiplier. */
  @Test def testSequentialMultiplier() {
    
    /** Build the sequential multiplier using input bus widths defined by n 
        and m. The width of the result bus should be equal to the sum 
        of the widths of input1 and input2, i.e., the sum of n and m. */
    val n = 8
    val m = 6
    val circuit = new Circuit()
    val input1 = circuit.inputs(n)
    val input2 = circuit.inputs(m)
    val loadEnable = circuit.input()
    val (ready, result) = 
      factory.buildSequentialMultiplier(input1, input2, loadEnable)
    
    /** Perform the test using the input pairs given in the following list. */
    val inputPairs = List((0, 0), (1, 0), (123, 7), (201, 49))
    for ((a, b) <- inputPairs) {
      
      /** Load the values to the multiplier and check that the value of ready 
          is not true before loadEnable is false. */
      setBusValues(input1, booleanOf(a, n))
      setBusValues(input2, booleanOf(b, m))
      loadEnable.set(true)
      assertEquals("Incorrect ready value. Inputs: %d, %d.".format(a, b), 
          false, ready.value)
      circuit.clock()
      assertEquals("Incorrect ready value. Inputs: %d, %d.".format(a, b), 
          false, ready.value)
      loadEnable.set(false)

      /** Clock the multiplier 2*(n+m)+1 times which should be enough to get 
          the result. */
      for (i <- 1 to 2*(n+m)+1) circuit.clock()
    
      /** Check the result. */
      assertTrue("Too many gates. " +
          "Number of gates: %d (bound: %d). ".format(circuit.numberOfGates(), 30*(n+m)) +
          "Inputs: %d, %d.".format(a, b), 
          circuit.numberOfGates() <= 30*(n+m))
      assertEquals("Incorrect ready value. Inputs: %d, %d.".format(a, b), 
          true, ready.value)
      assertEquals("Incorrect loadEnable value. Inputs: %d, %d.".format(a, b), 
          false, loadEnable.value)
      assertEquals("Incorrect result. Inputs: %d, %d.".format(a, b), 
          booleanOf(a*b, n+m), result.values)
    }
  }
}


