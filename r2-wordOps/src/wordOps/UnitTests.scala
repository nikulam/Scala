
package wordOps

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for wordOps.
 */
class UnitTests {

  /** Unit test for the popCount function. */
  @Test def testPopCount() {
    assertEquals("popCount(0x00000000)", 0,  popCount(0x00000000))
    assertEquals("popCount(0x11111111)", 8,  popCount(0x11111111))
    assertEquals("popCount(0xAAAAAAAA)", 16, popCount(0xAAAAAAAA))
    assertEquals("popCount(0xFFFFFFFF)", 32, popCount(0xFFFFFFFF))
  }

  /** Unit test for the reverse function. */
  @Test def testReverse() {
    assertEquals("reverse(0x0000.toShort)", 0x0000.toShort, reverse(0x0000.toShort))
    assertEquals("reverse(0xFFFF.toShort)", 0xFFFF.toShort, reverse(0xFFFF.toShort))
    assertEquals("reverse(0xAAAA.toShort)", 0x5555.toShort, reverse(0xAAAA.toShort))
    assertEquals("reverse(0x1234.toShort)", 0x2C48.toShort, reverse(0x1234.toShort))
  }

  /** Unit test for the reverse function. */
  @Test def testLeftRotate() {
    assertEquals("leftRotate(0x0000000000000000L,  0)",
        0x0000000000000000L, leftRotate(0x0000000000000000L,  0))
    assertEquals("leftRotate(0x8000000000000002L,  1)",
        0x0000000000000005L, leftRotate(0x8000000000000002L,  1))
    assertEquals("leftRotate(0x8000000000000002L, -2)",
        0xA000000000000000L, leftRotate(0x8000000000000002L, -2))
    assertEquals("leftRotate(0x1234567890ABCDEFL, 33)",
        0x21579BDE2468ACF1L, leftRotate(0x1234567890ABCDEFL, 33))
  }
}


