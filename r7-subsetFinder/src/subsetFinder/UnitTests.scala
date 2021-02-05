
package subsetFinder

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  def log2ceil(i: Int) = {
    def f(j: Int): Int = if (j <= 1) 1 else f(j / 2) + 1
    if (i <= 1) 0 else f(i - 1)
  }
  @Test def testOneSolution() {
    val rand = new scala.util.Random()
    val n = 10000
    val k = 2
    val domain = (1 to n).map(i => "e" + i).toSet
    val secretSubset = rand.shuffle(domain.toIndexedSeq).take(k).toSet
    val tester = new KSubsetTester(domain, secretSubset)
    val result = Finder.findOne(tester)
    assertTrue("An existing element not found", result != None)
    assertTrue("Incorrect answer", secretSubset contains result.get)
  }

  @Test def testOneCalls() {
    val rand = new scala.util.Random()
    val n = 100000
    val maxCalls = log2ceil(n) * 2 + 1
    val k = 2
    val domain = (1 to n).map(i => "e" + i).toSet
    val secretSubset = rand.shuffle(domain.toIndexedSeq).take(k).toSet
    val tester = new KSubsetTester(domain, secretSubset)
    val result = Finder.findOne(tester)
    val nofCalls = tester.nofCalls
    assertTrue("An existing element not found", result != None)
    assertTrue("Incorrect answer", secretSubset contains result.get)
    assertTrue("Too many calls (" + nofCalls + ", max " + maxCalls + " allowed)", nofCalls <= maxCalls)
  }

  @Test def testOneCalls2() {
    val rand = new scala.util.Random()
    val n = 100000
    val maxCalls = log2ceil(n) * 2 + 1
    val domain = (1 to n).map(i => "e" + i).toSet
    val secretSubset = Set(domain.last)
    val tester = new KSubsetTester(domain, secretSubset)
    val result = Finder.findOne(tester)
    val nofCalls = tester.nofCalls
    assertTrue("An existing element not found", result != None)
    assertTrue("Incorrect answer", secretSubset contains result.get)
    assertTrue("Too many calls (" + nofCalls + ", max " + maxCalls + " allowed)", nofCalls <= maxCalls)
  }

  @Test def testAllSolution() {
    val rand = new scala.util.Random()
    val n = 10000
    val k = 2
    val domain = (1 to n).map(i => "e" + i).toSet
    val secretSubset = rand.shuffle(domain.toIndexedSeq).take(k).toSet
    val tester = new KSubsetTester(domain, secretSubset)
    val result = Finder.findAll(tester)
    assertEquals("incorrect answer", secretSubset, result)
  }

  @Test def testAllCalls() {
    val rand = new scala.util.Random()
    val n = 100000
    val k = 5
    val maxCalls = k * log2ceil(n) * 2 + 1
    val domain = (1 to n).map(i => "e" + i).toSet
    val secretSubset = rand.shuffle(domain.toIndexedSeq).take(k).toSet
    val tester = new KSubsetTester(domain, secretSubset)
    val result = Finder.findAll(tester)
    val nofCalls = tester.nofCalls
    assertEquals("incorrect answer", secretSubset, result)
    assertTrue("Too many calls (" + nofCalls + ", max " + maxCalls + " allowed)", nofCalls <= maxCalls)
  }
  
 @Test def testAllCalls2() {
    val rand = new scala.util.Random()
    val n = 100000
    val maxCalls = 2 * log2ceil(n) * 2 + 1
    val domain = (1 to n).map(i => "e" + i).toSet
    val secretSubset = Set(domain.head, domain.last)
    val tester = new KSubsetTester(domain, secretSubset)
    val result = Finder.findAll(tester)
    val nofCalls = tester.nofCalls
    assertEquals("incorrect answer", secretSubset, result)
    assertTrue("Too many calls (" + nofCalls + ", max " + maxCalls + " allowed)", nofCalls <= maxCalls)
  }

}

