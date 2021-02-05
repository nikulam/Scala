
package base64

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for base64.
 */
class UnitTests {

  /*
   * The normal == operator between Array vals/vars just compares whether
   * they refer to the same object, not whether the arrays contain the same elements.
   * Thus we have this auxiliary function.
   */
  def areEqualArrays[T](a: Array[T], b: Array[T]): Boolean = {
    (a.length == b.length) && (a zip b).forall(x => x._1 == x._2)
  }

  /** Unit test for the to24Bits method. */
  @Test def testTo24Bits() {
    assertEquals(0xFF0000, to24Bits(0xFF, 0x00, 0x00))
    assertEquals(0x00FF00, to24Bits(0x00, 0xFF, 0x00))
    assertEquals(0x0000FF, to24Bits(0x00, 0x00, 0xFF))
    assertEquals(0x112233, to24Bits(0x11, 0x22, 0x33))
    assertEquals(0xFFFFFF, to24Bits(0xFF, 0xFF, 0xFF))
    /* The following test checks that you only take into account
     * the 8 least significant bits of each argument. */
    assertEquals(0x118800, to24Bits(0xffffff11, 0xffffff88, 0xffffff00))
  }

  /** Unit test for the to6BitWords method. */
  @Test def testTo6BitWords() {
    assertTrue(areEqualArrays(Array(0, 0, 0, 0x3F), to6BitWords(0x00003F)))
    assertTrue(areEqualArrays(Array(0, 0, 0x3F, 0), to6BitWords(0x000FC0)))
    assertTrue(areEqualArrays(Array(0, 0x3F, 0, 0), to6BitWords(0x03F000)))
    assertTrue(areEqualArrays(Array(0x3F, 0, 0, 0), to6BitWords(0xFC0000)))
    assertTrue(areEqualArrays(Array(63, 63, 63, 63), to6BitWords(0xFFFFFF)))
    assertTrue(areEqualArrays(Array(63, 47, 59, 62), to6BitWords(0xFEFEFE)))
  }

  /** Unit test for the restrictedEncode method. */
  @Test def testRestrictedEncode() {
    assertEquals("", restrictedEncode(Array[Byte]()))
    assertEquals("TWFu", restrictedEncode(Array[Byte](77, 97, 110)))
    assertEquals("////", restrictedEncode(Array[Byte](-1, -1, -1)))
    assertEquals("TWFu////", restrictedEncode(Array[Byte](77, 97, 110, -1, -1, -1)))
    assertEquals("/v7+", restrictedEncode(Array[Byte](-2, -2, -2)))
    assertEquals("7R+E4AG5jSsfPYwJWd4GgbBPL08jdo2v6F/b",
      restrictedEncode(Array(-19, 31, -124, -32, 1, -71, -115, 43, 31, 61, -116, 9, 89, -34, 6, -127, -80, 79, 47, 79, 35, 118, -115, -81, -24, 95, -37)))
  }

  /** Unit test for the encode function. */
  @Test def testEncode() {
    assertEquals("", encode(Array[Byte]()))
    assertEquals("/w==", encode(Array[Byte](-1)))
    assertEquals("/wE=", encode(Array[Byte](-1, 1)))
    assertEquals("AQID", encode(Array[Byte](1, 2, 3)))
    assertEquals("AQIDBA==", encode(Array[Byte](1, 2, 3, 4)))
    assertEquals("YW55IGNhcm5hbCBwbGVhc3VyZS4=", encode("any carnal pleasure.".getBytes("UTF-8")))
    assertEquals("YW55IGNhcm5hbCBwbGVhc3VyZQ==", encode("any carnal pleasure".getBytes("UTF-8")))
    assertEquals("YW55IGNhcm5hbCBwbGVhc3Vy", encode("any carnal pleasur".getBytes("UTF-8")))
    assertEquals("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=", encode("Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.".getBytes("UTF-8")))
  }
}


