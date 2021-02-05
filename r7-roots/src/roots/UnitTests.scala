
package roots

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests.
 */
class UnitTests {
  val maxError = 0.001
  def isWithin(v: Double, correct: Double) = (correct * (1.0 - maxError) <= v) && (v <= correct * (1.0 + maxError))
  @Test def testRoot1() {
    /* Note: math.log is base e (natural logarithm) */
    val f = { v: Double => math.log(v) + 2 * v }
    val result = zero(f, 0.001, 1000)
    val correct = 0.42630275100686277
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
  }

  @Test def testRoot2() {
    val f = { v: Double => math.sin(math.toRadians(v)) - 0.5 }
    val result = zero(f, 0.001, 90)
    val correct = 30.0
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
  }

  /* A correct binary search method should not make too many calls to f */
  @Test def testRoot3() {
    var nofCalls = 0
    val f = { v: Double => { nofCalls += 1; v - 0.9E20 } }
    val result = zero(f, -1E20, 1E20)
    val correct = 0.9E20
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
    assertTrue("made too many calls (" + nofCalls + ") to f", nofCalls < 1000)
  }

  @Test def testSquareRoot() {
    val result = nthRoot(2)(2.0)
    val correct = 1.414213562373095
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
  }

  @Test def testCubeRoot() {
    val result = nthRoot(3)(8.0)
    val correct = 2.0
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
  }

  @Test def testFourthRoot() {
    val result = nthRoot(4)(1025)
    val correct = 5.658234811950123
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
  }

  @Test def testFifthRoot() {
    val result = nthRoot(5)(2105)
    val correct = 4.620089832752335
    assertTrue("solution " + result + " is not close enough to " + correct, isWithin(result, correct))
  }
}


