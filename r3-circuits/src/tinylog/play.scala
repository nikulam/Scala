
package tinylog

/*
 * Run this object to play with your designs in Toggler.
 * If you want to play with other methods than the adder, 
 * just modify the code below.
 *
 */ 
object play {
  def main(args: Array[String]): Unit = {
    val toggler = new Toggler()
    val n = 4
    val aa = Bus.inputs(n)
    val bb = Bus.inputs(n)
    val cc = factory.buildAdder(aa, bb)
    toggler.watch("aa", aa.reverse)
    toggler.watch("bb", bb.reverse)
    toggler.watch("cc", cc.reverse)
    toggler.go()
  }
}


