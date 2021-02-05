
package withProb

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  @Test def test1 {
    var x = 0
    val n = 100000
    val prob = 30.0
    for (i <- 1 to n) {
      doWithProb(prob) { x += 1 }
    }
    /* This should hold with a high probability for large values of n */
    val expected = n * prob / 100.0
    assertTrue(0.95 * expected <= x && x <= expected * 1.05)
  }

  @Test def test2 {
    val x = 2
    val y = 3
    val z: Int = withProb(30.0) { x * y } otherwise { 0 }
    assertTrue(z == 6 || z == 0)
    assertEquals(5, withProb(0.0) { z * x + y } otherwise { x + y })
  }
}


