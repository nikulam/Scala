
package tinylog

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  @Test def testXor() {
    val a = new InputElement()
    val b = new ConstantGate(false)
    val g1 = new XorGate(a, b)
    a.set(true)
    assertEquals("wrong value", true, g1.value)
  }

  @Test def testMajority() {
    val a = new InputElement()
    val b = new ConstantGate(false)
    val c = new InputElement()
    val g1 = new MajorityGate(a, b, c)
    a.set(true)
    c.set(false)
    assertEquals("wrong value", false, g1.value)
  }

  @Test def testDepth() {
    val a = new InputElement()
    val b = new ConstantGate(false)
    val c = new ConstantGate(false)
    val g1 = new AndGate(a, b)
    val g2 = new OrGate(g1, b)
    val g3 = new AndGate(c, g2)
    assertEquals("wrong depth for g3", 3, g3.depth)
  }

  @Test def testSupport() {
    val a = new InputElement()
    val b = new ConstantGate(false)
    val c = new ConstantGate(false)
    val g1 = new AndGate(a, b)
    val g2 = new OrGate(g1, b)
    val g3 = new AndGate(c, g2)
    val expected = Set(g2, g1, b, a)
    assertEquals("wrong support for g2", expected, g2.support)
  }

  @Test def testInputElementSupport() {
    val a = new InputElement()
    val b = new ConstantGate(false)
    val c = new ConstantGate(false)
    val g1 = new AndGate(a, b)
    val g2 = new OrGate(g1, b)
    val g3 = new AndGate(c, g2)
    val expected = Set(a)
    assertEquals("wrong input element support for g2", expected, g2.inputElementSupport)
  }
}


