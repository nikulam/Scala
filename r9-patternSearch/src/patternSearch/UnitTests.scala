
package patternSearch

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  @Test def testN123() {
    assertTrue("testN123() failed", { val (a,b) = search("","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 0) && (b == 11) } )
  }
  @Test def testN124() {
    assertTrue("testN124() failed", { val (a,b) = search("i","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 0) && (b == 4) } )
  }
  @Test def testN125() {
    assertTrue("testN125() failed", { val (a,b) = search("m","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 4) && (b == 5) } )
  }
  @Test def testN126() {
    assertTrue("testN126() failed", { val (a,b) = search("s","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 7) && (b == 11) } )
  }
  @Test def testN127() {
    assertTrue("testN127() failed", { val (a,b) = search("p","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 5) && (b == 7) } )
  }
  @Test def testN128() {
    assertTrue("testN128() failed", { val (a,b) = search("a","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
  @Test def testN129() {
    assertTrue("testN129() failed", { val (a,b) = search("q","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
  @Test def testN130() {
    assertTrue("testN130() failed", { val (a,b) = search("z","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
  @Test def testN131() {
    assertTrue("testN131() failed", { val (a,b) = search("sisss","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
  @Test def testN132() {
    assertTrue("testN132() failed", { val (a,b) = search("isis","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
  @Test def testN133() {
    assertTrue("testN133() failed", { val (a,b) = search("issi","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 2) && (b == 4) } )
  }
  @Test def testN134() {
    assertTrue("testN134() failed", { val (a,b) = search("mississippi","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  (a == 4) && (b == 5) } )
  }
  @Test def testN135() {
    assertTrue("testN135() failed", { val (a,b) = search("mississippiz","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
  @Test def testN136() {
    assertTrue("testN136() failed", { val (a,b) = search("mississippia","mississippi",Vector(10, 7, 4, 1, 0, 9, 8, 6, 3, 5, 2));  a == b } )
  }
}


