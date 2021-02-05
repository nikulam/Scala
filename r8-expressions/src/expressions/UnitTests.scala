
package expressions

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests.
 */
class UnitTests {
  @Test def testVars() {
    val x = Var("x")
    val y = Var("y")
    val e = x * y + 3.0 * y
    assertEquals(Set("x", "y"), e.vars)
  }
  @Test def testEvaluate() {
    val x = Var("x")
    val y = Var("y")
    val e = x + 3.0 * y
    // Note the third parameter: for floats, assertEquals requires the "delta" or error tolerance
    assertEquals(14.0, e.evaluate(Map("x" -> 2.0, "y" -> 4.0)), 0.0)
  }
  @Test def testPartialEvaluate() {
    val x = Var("x")
    val y = Var("y")
    val e = x + -3.0 * y
    assertEquals(Set("x", "y"), e.vars)
    val e2 = e.partialEvaluate(Map("x" -> 2.0))
    assertEquals(Set("y"), e2.vars)
    // Note the third parameter: for floats, assertEquals requires the "delta" or error tolerance
    assertEquals(-10.0, e2.evaluate(Map("y" -> 4.0)), 0.0)
  }
  @Test def testPartialDerivative() {
    val x = Var("x")
    val y = Var("y")
    val e = x*x + -3.0 * y * x + y * y * 7.0
    assertEquals(Set("x", "y"), e.vars)
    val e2 = e.partialDerivative("x")
    // The result should be equivalent to 2x - 3y
    assertEquals(Set("x", "y"), e2.vars)
    // Note the third parameter: for floats/doubles, assertEquals requires the "delta" or error tolerance
    assertEquals(2.0, e2.evaluate(Map("x" -> 7.0, "y" -> 4.0)), 0.0)
  }
  @Test def testIsInSOP() {
    val x = Var("x")
    val y = Var("y")
    val z = Var("z")
    assertTrue(x.isInSOP)
    assertTrue((-x).isInSOP)
    assertTrue((-x * -y + -2.0 * x + z).isInSOP)
    assertFalse((-x * -y - 2.0 * x + z).isInSOP)
    assertFalse((-x * y - 2.0 * (x + z)).isInSOP)
  }
  @Test def testExpand() {
    val x = Var("x")
    val y = Var("y")
    val z = Var("z")
    val e = 3 * x * (-2 * y - (z * y + y * -3) * (-2 * y + 7 * x)) + 7 * y
    val eSOM = e.expand
    assertTrue(eSOM.isInSOP)
    /* Check that the expressions evaluate to the same value in a point */
    val p = Map("x" -> 2.0, "y" -> -5.0, "z" -> 11.0)
    assertEquals(e.evaluate(p), eSOM.evaluate(p), 0.0)
  }
}


