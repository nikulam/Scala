
package iterators

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  @Test def testPrimes() {
    val it = primesIterator
    assertEquals(Vector(1, 2, 3, 5, 7, 11, 13, 17, 19, 23), it.take(10).toVector)
  }

  @Test def testPrimesMany() {
    /* This test should finish in a bit less than one second in a good desktop machine with 3.1GHz CPU or similar */
    val it = primesIterator
    var result = 0
    (1 to 100000).foreach(i => { result = it.next() })
    assertEquals(1299689, result)
  }

  @Test def testBlockingRandom() {
    val s = Set(1, 2, 3, 4, 5, 6)
    val it = blockingRandomIterator(s, 2)
    var prevprev: Option[Int] = None
    var prev: Option[Int] = None
    val counts = scala.collection.mutable.HashMap[Int, Int]()
    s.foreach(e => counts(e) = 0)
    val n = 12000
    for (i <- 1 to n) {
      val e = it.next
      assertTrue(s contains e)
      counts(e) += 1
      assertTrue(Some(e) != prevprev)
      assertTrue(Some(e) != prev)
      prevprev = prev
      prev = Some(e)
    }
    /* The following should hold with reasonably high probability for large enough n */
    val expected = n/s.size
    assertTrue(counts.forall({case (e, count) => (expected*0.95 <= count && count <= 1.05*expected)}))
  }

}


