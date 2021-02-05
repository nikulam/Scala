
package pairSum
import timer._
import imageShower._
import scala.collection.mutable.ArrayBuffer

/**
 * Run the methods hasPair and hasPairSlow with some instances, plot the results.
 * The default is to use D3.js as the plotter. In this case the result, the html-file plot.html,
 *   is output in the htmlPlot-folder of this project.
 * If you want to use gnoplot instead, set the "method" value to "gnuplot". In this case the resulting plot
 *   is shown with ImageShower graphical applet. Requires that gnuplot is installed in the machine.
 */
object plotTest {
  val method = "D3"

  import scala.util.Random
  val prng = new Random(2105)
  def randInt(max: Int, rem: Int, mod: Int) = (prng.nextInt(max) / mod) * mod + rem
  def randList(n: Int, rem: Int, mod: Int) = {
    require(n >= 0)
    require(mod >= 2)
    require(0 <= rem && rem < mod)
    (1 to n).map(v => randInt(1000000000, rem, mod))
  }

  def main(args: Array[String]): Unit = {
    type DataPoint = Tuple2[Int, Double]
    val resultsNlogN = new ArrayBuffer[DataPoint]()
    val resultsQuadratic = new ArrayBuffer[DataPoint]()
    for (n <- 10000 to 100000 by 10000) {
      Console.println("Measuring with n = " + n)
      // Create two lists of n elements and a target t so that 
      // the target value is not the sum of two elements in the lists
      val l1 = randList(n, 3, 17).toList
      val l2 = randList(n, 6, 17).toList
      val t = randInt(2000000000, 8, 17)
      // Run faster method and collect running time
      val (r1, t1) = measureCpuTimeRepeated(hasPair(l1, l2, t))
      resultsNlogN += ((n, t1))
      Console.println("  n log n algorithm took " + t1 + " seconds")
      // Run slower method and collect running time
      val (r2, t2) = measureCpuTimeRepeated(hasPairSlow(l1, l2, t))
      resultsQuadratic += ((n, t2))
      Console.println("  quadratic algorithm took " + t2 + " seconds")
    }
    val dsNlogN = new plotter.DataSet("n log n", resultsNlogN)
    val dsQuadratic = new plotter.DataSet("quadratic", resultsQuadratic)
    if (method == "D3") {
      val d3plotter = new plotter.D3Plotter()
      d3plotter.xLogScale = false
      d3plotter.yLogScale = true
      d3plotter.plot("n", "run time (seconds)", dsNlogN, dsQuadratic)
      Console.println("The plot has now been produced in the folder htmlPlot of this project")
    } else {
      val gnuPlotter = new plotter.GnuPlotter()
      gnuPlotter.xLogScale = false
      gnuPlotter.yLogScale = true
      val pngFile = gnuPlotter.plot("n", "run time (seconds)", dsNlogN, dsQuadratic)
      if (pngFile != null) {
        val s = new ImageShower(pngFile) { override def closeOperation { pngFile.delete(); dispose } }
      }
    }
  }
}


