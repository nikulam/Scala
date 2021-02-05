
package tinylog

import org.junit.Test
import org.junit.Assert._

class UnitTests {

  def setBus(b: Bus, values: Boolean*) {
    require(b.forall(g => g.isInstanceOf[InputElement]), "The bus must consists of input elements only")
    require(values.length == b.length, "The number of values must match with bus width")
    (b zip values).foreach({ case (g: InputElement, v: Boolean) => g.set(v); case _ => require(false) })
  }

  @Test def testReset() {
    val n = 4
    val r0 = Bus.inputs(n)
    val r1 = Bus.inputs(n)
    val r2 = Bus.inputs(n)
    val r3 = Bus.inputs(n)
    val data = Bus.inputs(n)
    val writeEnable = Gate.input()
    val writeTo = Bus.inputs(2)
    val reset = Gate.input()
    // Build the unit
    val (s0, s1, s2, s3) = factory.buildBusWriteUnit4(r0, r1, r2, r3, data, writeEnable, writeTo, reset)
    // OK the unit is now built, so let us feed in some input
    setBus(r0, true, false, false, false)
    setBus(r1, true, false, true, false)
    setBus(r2, true, false, false, true)
    setBus(r3, false, false, false, false)
    setBus(data, true, true, true, false)
    writeEnable.set(true)
    setBus(writeTo, true, false)
    reset.set(true)
    // Check that the unit gives the correct output
    assertEquals("Incorrect output on s0", Seq(false, false, false, false), s0.values)
    assertEquals("Incorrect output on s1", Seq(false, false, false, false), s1.values)
    assertEquals("Incorrect output on s2", Seq(false, false, false, false), s2.values)
    assertEquals("Incorrect output on s3", Seq(false, false, false, false), s3.values)
  }

  @Test def testNoWrite() {
    val n = 4
    val r0 = Bus.inputs(n)
    val r1 = Bus.inputs(n)
    val r2 = Bus.inputs(n)
    val r3 = Bus.inputs(n)
    val data = Bus.inputs(n)
    val writeEnable = Gate.input()
    val writeTo = Bus.inputs(2)
    val reset = Gate.input()
    // Build the unit
    val (s0, s1, s2, s3) = factory.buildBusWriteUnit4(r0, r1, r2, r3, data, writeEnable, writeTo, reset)
    // OK the unit is now built, so let us feed in some input
    setBus(r0, true, false, false, false)
    setBus(r1, true, false, true, false)
    setBus(r2, true, false, false, true)
    setBus(r3, false, false, false, false)
    setBus(data, true, true, true, false)
    writeEnable.set(false)
    setBus(writeTo, true, false)
    reset.set(false)
    // Check that the unit gives the correct output
    assertEquals("Incorrect output on s0", Seq(true, false, false, false), s0.values)
    assertEquals("Incorrect output on s1", Seq(true, false, true, false), s1.values)
    assertEquals("Incorrect output on s2", Seq(true, false, false, true), s2.values)
    assertEquals("Incorrect output on s3", Seq(false, false, false, false), s3.values)
  }

  @Test def testWrite1() {
    val n = 4
    val r0 = Bus.inputs(n)
    val r1 = Bus.inputs(n)
    val r2 = Bus.inputs(n)
    val r3 = Bus.inputs(n)
    val data = Bus.inputs(n)
    val writeEnable = Gate.input()
    val writeTo = Bus.inputs(2)
    val reset = Gate.input()
    // Build the unit
    val (s0, s1, s2, s3) = factory.buildBusWriteUnit4(r0, r1, r2, r3, data, writeEnable, writeTo, reset)
    // OK the unit is now built, so let us feed in some input
    setBus(r0, true, false, false, false)
    setBus(r1, true, false, true, false)
    setBus(r2, true, false, false, true)
    setBus(r3, false, false, false, false)
    setBus(data, true, true, true, false)
    writeEnable.set(true)
    setBus(writeTo, true, false)
    reset.set(false)
    // Check that the unit gives the correct output
    assertEquals("Incorrect output on s0", Seq(true, false, false, false), s0.values)
    assertEquals("Incorrect output on s1", Seq(true, true, true, false), s1.values)
    assertEquals("Incorrect output on s2", Seq(true, false, false, true), s2.values)
    assertEquals("Incorrect output on s3", Seq(false, false, false, false), s3.values)
  }
}


