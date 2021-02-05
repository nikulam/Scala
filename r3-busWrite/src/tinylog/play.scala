
package tinylog

/*
 * Run this object to play with the bus write unit in Toggler.
 *
 */

object play {
  def main(args: Array[String]): Unit = {
    val toggler = new Toggler()
    val n = 8
    val r0 = Bus.inputs(n)
    val r1 = Bus.inputs(n)
    val r2 = Bus.inputs(n)
    val r3 = Bus.inputs(n)
    val data = Bus.inputs(n)
    val writeEnable = Gate.input()
    val writeTo = Bus.inputs(2)
    val reset = Gate.input()
    val (s0, s1, s2, s3) = factory.buildBusWriteUnit4(r0, r1, r2, r3, data, writeEnable, writeTo, reset)
    toggler.clear()
    toggler.watch("r0", r0.reverse)
    toggler.watch("r1", r1.reverse)
    toggler.watch("r2", r2.reverse)
    toggler.watch("r3", r3.reverse)
    toggler.watch("data", data.reverse)
    toggler.watch("writeEnable", writeEnable)
    toggler.watch("writeTo", writeTo.reverse)
    toggler.watch("reset", reset)
    toggler.watch("s0", s0.reverse)
    toggler.watch("s1", s1.reverse)
    toggler.watch("s2", s2.reverse)
    toggler.watch("s3", s3.reverse)
    toggler.go()
  }
}


