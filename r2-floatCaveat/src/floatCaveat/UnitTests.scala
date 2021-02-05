
package floatCaveat

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for floatCaveat.
 */
class UnitTests {

  /** Unit test for the values x, y, z. */
  @Test def testValues() {
    // The last argument in assertEquals denotes the error allowed in the 
    // comparison. 
    assertEquals(1.0, (x + y) + z, 0.0)
    assertEquals(0.0, x + (y + z), 0.0)
  }

}


