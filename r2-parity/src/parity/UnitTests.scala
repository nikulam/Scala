
package parity

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for parity.
 */
class UnitTests {

  /** Unit test for the extend method. */
  @Test def testExtend() {
    assertEquals("extend(0x0000000000000000L)", 0x0000000000000000L, extend(0x0000000000000000L))
    assertEquals("extend(0x0000000000000001L)", 0x8000000000000001L, extend(0x0000000000000001L))
    assertEquals("extend(0x0000000000000011L)", 0x0000000000000011L, extend(0x0000000000000011L))
    assertEquals("extend(0x8000000000000000L)", 0x0000000000000000L, extend(0x8000000000000000L))
    assertEquals("extend(0x8000000000F00000L)", 0x0000000000F00000L, extend(0x8000000000F00000L))
    assertEquals("extend(0x0000001000300000L)", 0x8000001000300000L, extend(0x0000001000300000L))
  }
  
  /** Unit test for the ok method. */
  @Test def testOk() {
    assertTrue("ok(0x0000000000000000L)", ok(0x0000000000000000L))
    assertTrue("ok(0x8000000000000001L)", ok(0x8000000000000001L))
    assertTrue("ok(0x0000000000000011L)", ok(0x0000000000000011L))
    assertTrue("ok(0x0000000000000000L)", ok(0x0000000000000000L))
    assertTrue("ok(0x0000000000F00000L)", ok(0x0000000000F00000L))
    assertTrue("ok(0x8000001000300000L)", ok(0x8000001000300000L))
    assertTrue("!ok(0x0000000100000000L)", !ok(0x0000000100000000L))
    assertTrue("!ok(0x8000000001000001L)", !ok(0x8000000001000001L))
    assertTrue("!ok(0x0000010000000011L)", !ok(0x0000010000000011L))
    assertTrue("!ok(0x8000000000000000L)", !ok(0x8000000000000000L))
    assertTrue("!ok(0x0000000000E00000L)", !ok(0x0000000000E00000L))
    assertTrue("!ok(0x8000005000300000L)", !ok(0x8000005000300000L))
  }
}


