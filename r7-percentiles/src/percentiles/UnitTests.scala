
package percentiles

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests.
 */
class UnitTests {
  def nofLessOrEqual[T](seq: Seq[T], v: T)(implicit ord: scala.math.Ordering[T]) =
    seq.count(u => ord.lteq(u, v))

  /** The zeroth percentile should be the smallest element */
  @Test def testPercentileMin() {
    val l = List(4, 3, 7, 3, 11, 5, 7, 8, 99, 2, 6)
    val n = l.length
    val result = percentile(0)(l)
    assertEquals(l.min, result)
  }

  /** Check correctness of percentile for p = {1,2,...,100} */
  @Test def testPercentileOthers() {
    val l = List(3, 31, 32, 32, 17, 29, 16, 31, 15, 0, 14, 35, 31, 2, 29, 13, 10, 16)
    val n = l.length
    for (p <- 1 to 100) {
      val result = percentile(p)(l)
      // Check that at least p percent of the elements are less or equal to "result"
      val nofLE = nofLessOrEqual(l, result)
      assertTrue(nofLE * 100.0 / n >= p)
      // Check that "result" is the smallest element for which
      // at least p percent of the elements are less or equal to "result"
      val nofLT = nofLessOrEqual(l, result - 1)
      assertTrue(nofLT * 100.0 / n < p)
    }
  }

  /** Check that percentile does not perform too many comparisons */
  @Test def testPercentileEfficiency() {
    // The number of comparisons made in "percentile" function will be counted with these
    var nofComparisons = 0
    object myCountingIntOrdering extends scala.math.Ordering[Int] {
      def compare(a: Int, b: Int) = { nofComparisons += 1; a compare b }
    }
    // Test sequence with some pseudo-random integers
    val rand = new scala.util.Random(2105)
    val n = 1000
    val l = (1 to n).map(i => rand.nextInt(10000))
    // Compute the median and secretly count the number of comparisons made during the computation
    val result = percentile(50)(l)(myCountingIntOrdering)
    // Check that the implementation of "percentile" is efficient enough
    assertTrue(nofComparisons <= n * math.log(n) / math.log(2))
  }

}


