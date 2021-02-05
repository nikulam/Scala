
package pairSum

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests for hasPairSlow and hasPair.
 */
class UnitTests {
    val l1 = List(11, -30, 12, 7)
    val l2 = List(12, -2, 101)

  @Test def testSlowTrue() {
    assertEquals("did not find solution", Some((12,-2)), hasPairSlow(l1, l2, 10))
  }
  @Test def testSlowFalse() {
    assertEquals("found a bad solution", None, hasPairSlow(l1, l2, 11))
  }
  @Test def testFastTrue() {
    assertEquals("did not find solution", Some((12,-2)), hasPair(l1, l2, 10))
  }
  @Test def testFastFalse() {
    assertEquals("found a bad solution", None, hasPair(l1, l2, 11))
  }
}


