
package pascal

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests for Pascal's triangle.
 */
class UnitTests {
  @Test def test1() {
    assertEquals("value at (0,0)", 1, getCoefficient(0, 0))
  }
  @Test def test2() {
    assertEquals("value at (4,0)", 1, getCoefficient(4, 0))
  }
  @Test def test3() {
    assertEquals("value at (5,5)", 1, getCoefficient(5, 5))
  }
  @Test def test4() {
    assertEquals("value at (4,1)", 4, getCoefficient(4, 1))
  }
  @Test def test5() {
    assertEquals("value at (5,2)", 10, getCoefficient(5, 2))
  }
}


