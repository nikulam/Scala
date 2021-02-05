
package linkedLists

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests.
 * Note that these are not part of your submission and
 * one is allowed to use, for example, the collection.Range classes here.
 */
class UnitTests {

  @Test def testReverse() {
    val l = Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))
    assertEquals(Cons(1, Cons(3, Cons(2, Cons(1, Nil())))), l.reverse)
  }

  /* If this fails due to StackOverflowError, the method 'reverse' is probably not tail recursive */
  @Test def testReverseLarge() {
    val n = 100000
    val m = 30000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i % m, l))
    val l2 = (1 to n).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i % m, l))
    assertEquals(l2, l.reverse)
  }

  @Test def testLength() {
    val l = Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))
    assertEquals(4, l.length)
  }

  /* If this fails due to StackOverflowError, the method 'length' is probably not tail recursive */
  @Test def testLengthLarge() {
    val n = 100000
    val m = 30000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i % m, l))
    assertEquals(n, l.length)
  }

  @Test def testContainsSmall() {
    val l = Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))
    assertEquals(true, l.contains(3))
    assertEquals(false, l.contains(4))
  }

  /* If this fails due to StackOverflowError, the method 'contains' is probably not tail recursive */
  @Test def testContainsLarge() {
    val n = 100000
    val l = (1 to n).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals(true, l.contains(n / 2))
  }

  @Test def testCountSmall() {
    val l = Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))
    assertEquals(2, l.count(_ == 1))
    assertEquals(0, l.count(_ == 4))
  }
  /* If this fails due to StackOverflowError, the method 'count' is probably not tail recursive */
  @Test def testCountLarge() {
    val n = 100000
    val m = 30000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i % m, l))
    assertEquals(4, l.count(_ == 3))
  }

  @Test def testFilterNotSmall() {
    val l = Cons(1, Cons(2, Cons(3, Cons(4, Nil()))))
    assertEquals(Cons(1, Cons(3, Nil())), l.filterNot(_ % 2 == 0))
  }
  /* If this fails due to StackOverflowError, the method 'filterNot' is probably not tail recursive */
  @Test def testFilterNotLarge() {
    val n = 100000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals(Cons(3, Cons(50003, Nil())), l.filterNot(_ % 50000 != 3))
  }

  @Test def testLastSmall() {
    val l = Cons(1, Cons(7, Cons(2, Cons(11, Nil()))))
    assertEquals(11, l.last)
  }
  @Test(expected = classOf[NoSuchElementException])
  def testLastSmallNil {
    val l = Nil()
    val v = l.last
  }
  /* If this fails due to StackOverflowError, the method 'last' is probably not tail recursive */
  @Test def testLastLarge() {
    val n = 100000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals(n, l.last)
  }

  @Test def testDropSmall() {
    val l = Cons(1, Cons(2, Cons(3, Cons(4, Nil()))))
    assertEquals(l, l.drop(0))
    assertEquals(Cons(3, Cons(4, Nil())), l.drop(2))
    assertEquals(Nil(), l.drop(10))
  }
  /* If this fails due to StackOverflowError, the method 'drop' is probably not tail recursive */
  @Test def testDropLarge() {
    val n = 100000
    val l = (2 * n).to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    val m = (2 * n).until(n, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals(m, l.drop(n))
  }

  @Test def testMapSmall() {
    val l = Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))
    assertEquals(Cons("x2", Cons("x3", Cons("x4", Cons("x2", Nil())))), l.map(i => "x" + (i + 1)))
  }
  /* If this fails due to StackOverflowError, the method 'map' is probably not tail recursive */
  @Test def testMapLarge() {
    val n = 100000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    val lsucc = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i + 1, l))
    assertEquals(lsucc, l.map(_ + 1))
  }

  @Test def testFoldLeftSmall() {
    val l = Cons(3, Cons(1, Cons(2, Cons(1, Nil()))))
    // Compute 3*1+1*2+2*3+1*4 = 15
    assertEquals(15, l.foldLeft((0, 1))({ case ((sum, index), e) => (sum + e * index, index + 1) })._1)
  }
  /* If this fails due to StackOverflowError, the method 'foldLeft' is probably not tail recursive */
  @Test def testFoldLeftLarge() {
    val n = 100000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals(1626540144, l.foldLeft((0, 1))({ case ((sum, index), e) => (sum + e * index, index + 1) })._1)
  }

  @Test def testFoldRightSmall() {
    val l = Cons(3, Cons(1, Cons(2, Cons(1, Nil()))))
    // Compute 1*1+2*2+1*3+3*4 = 20
    assertEquals(20, l.foldRight((0, 1))({ case (e, (sum, index)) => (sum + e * index, index + 1) })._1)
  }
  /* If this fails due to StackOverflowError, the method 'foldRight' is probably not tail recursive */
  @Test def testFoldRightLarge() {
    val n = 100000
    val l = n.to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals(166671666700000L, l.foldRight((0L, 1L))({ case (e, (sum, index)) => (sum + e * index, index + 1) })._1)
  }

  @Test def testSplitAtSmall() {
    val l = Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))
    assertEquals((Cons(1, Cons(2, Nil())), Cons(3, Cons(1, Nil()))), l.splitAt(2))
    assertEquals((Nil(), Cons(1, Cons(2, Cons(3, Cons(1, Nil()))))), l.splitAt(0))
    assertEquals((Cons(1, Cons(2, Cons(3, Cons(1, Nil())))), Nil()), l.splitAt(5))
  }
  /* If this fails due to StackOverflowError, the method 'splitAt' is probably not tail recursive */
  @Test def testSplitAtLarge() {
    val n = 100000
    val l = (2 * n).to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    val p1 = (n).to(1, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    val p2 = (2 * n).until(n, -1).foldLeft[LinkedList[Int]](Nil())((l, i) => Cons(i, l))
    assertEquals((p1, p2), l.splitAt(n))
  }
}


