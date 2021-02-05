
package suffixBuild

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  @Test def testN123() {
    assertTrue("testN123() failed", (suffixArray("alpakkajakkasakkavakkanakka") zip Vector(26, 6, 23, 3, 18, 8, 13, 0, 21, 11, 16, 7, 25, 5, 20, 10, 15, 24, 4, 19, 9, 14, 1, 22, 2, 12, 17)).forall(x => x._1 == x._2))
  }
  @Test def testN124() {
    assertTrue("testN124() failed", (suffixArray("donau") zip Vector(3, 0, 2, 1, 4)).forall(x => x._1 == x._2))
  }
  @Test def testN125() {
    assertTrue("testN125() failed", (suffixArray("nilenil") zip Vector(3, 5, 1, 6, 2, 4, 0)).forall(x => x._1 == x._2))
  }
  @Test def testN126() {
    assertTrue("testN126() failed", (suffixArray("danube") zip Vector(1, 4, 0, 5, 2, 3)).forall(x => x._1 == x._2))
  }
  @Test def testN127() {
    assertTrue("testN127() failed", (suffixArray("aavankaavanjaavanlaavanhaavan") zip Vector(24, 18, 6, 0, 12, 27, 21, 9, 3, 15, 25, 19, 7, 1, 13, 23, 11, 5, 17, 28, 22, 10, 4, 16, 26, 20, 8, 2, 14)).forall(x => x._1 == x._2))
  }
  @Test def testN128() {
    assertTrue("testN128() failed", (suffixArray("xyzw") zip Vector(3, 0, 1, 2)).forall(x => x._1 == x._2))
  }
  @Test def testN129() {
    assertTrue("testN129() failed", (suffixArray("abracadabra") zip Vector(10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2)).forall(x => x._1 == x._2))
  }
  @Test def testN130() {
    assertTrue("testN130() failed", (suffixArray("foobar") zip Vector(4, 3, 0, 2, 1, 5)).forall(x => x._1 == x._2))
  }
  @Test def testN131() {
    assertTrue("testN131() failed", (suffixArray("mississippi") zip Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2)).forall(x => x._1 == x._2))
  }
  @Test def testN132() {
    assertTrue("testN132() failed", (suffixArray("") zip Vector[Int]()).forall(x => x._1 == x._2))
  }
}


