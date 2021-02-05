
package plotter

import java.io.{ File, FileWriter }
import java.io.IOException
import scala.collection.mutable.ArrayBuffer
import scala.sys.process._

/**
 * A simple class for plotting data sets with gnuplot.
 * @author Tommi Junttila
 */
class GnuPlotter {
  private var _xLogScale = false
  def xLogScale = _xLogScale
  /** Set/unset the x-axis to use logarithmic scale (use "plotter.xLogScale = true" to set log-scale). */
  def xLogScale_=(v: Boolean): Unit = _xLogScale = v

  private var _yLogScale = false
  def yLogScale = _yLogScale
  /** Set/unset the y-axis to use logarithmic scale (use "plotter.yLogScale = true" to set log-scale). */
  def yLogScale_=(v: Boolean): Unit = _yLogScale = v

  private var _width = 900
  /** Get the width of the plot */
  def width = _width
  /** Set the width of the plot */
  def width_=(v: Int) { require(0 < v && v < 4096); _width = v }

  private var _height = 600
  /** Get the height of the plot */
  def height = _height
  /** Set the height of the plot */
  def height_=(v: Int) { require(0 < v && v < 3072); _height = v }

  private var _linesType = "linespoints"
  def linesType = _linesType
  def linesType_=(t: String) { require(t == "lines" || t == "linespoints"); _linesType = t }

  private var _key = "left top"
  def key = _key
  def key_=(k: String) { require(Set("left top", "left bottom", "right top", "right bottom") contains k); _key = k }

  private var _yrange: Option[Tuple2[Double,Double]] = None
  def yrange = _yrange
  def yrange_=(p:Tuple2[Double,Double]) = {require(p._1 < p._2); _yrange = Some(p)}
  def yrange_=(s: String) = {require(s == "auto"); _yrange = None}

  /**
   * Plot the argument data sets with gnuplot.
   * @return the plot as a PNG file, null if plotting failed for some reason.
   */
  def plot[XT, YT](xAxisName: String, yAxisName: String, dataSets: DataSet[XT, YT]*): File = {
    require(dataSets.length > 0, "Must provide at least one data set")
    /* Write data sets in temporary files */
    val files = new ArrayBuffer[File]()
    try {
      for (dataSet <- dataSets) {
        val f = File.createTempFile("data", ".csv")
        files.append(f)
        var ostream: FileWriter = null
        try {
          ostream = new FileWriter(f)
          ostream.write("x, " + dataSet.name + "\n")
          for ((x, y) <- dataSet.points) {
            ostream.write(x + ", " + y + "\n")
          }
        } finally {
          if (ostream != null) ostream.close()
        }
      }
    } catch {
      case e: IOException => files.foreach(f => f.delete()); return null
    }
    /* Write gnuplot command file in temporary file, or feed it in as stream? */
    val gnuplotFile = File.createTempFile("commands", ".plt")
    val pngFile = File.createTempFile("plot", ".png")
    val yrangeString = yrange match {
      case None => ""
      case Some((l,h)) => "set yrange ["+l+":"+h+"]"
    }
    try {
      var gnuplotWriter: FileWriter = null
      try {
        gnuplotWriter = new FileWriter(gnuplotFile)
        gnuplotWriter.write("""set term png enhanced size """ + width + """,""" + height + """
set out '""" + pngFile.getCanonicalPath() + """'
set datafile separator ','
set key """+key+"""
set key autotitle columnhead
""" + (if (xLogScale) "set logscale x" else "") + """
""" + (if (yLogScale) "set logscale y" else "") + """
""" + yrangeString + """
set xlabel '""" + xAxisName + """'
set ylabel '""" + yAxisName + """'
plot """ + files.map(f => "'" + f.getCanonicalPath() + "' using 1:2 with " + linesType).mkString(",") + """
quit
""")
      } finally {
        if (gnuplotWriter != null) gnuplotWriter.close()
      }
    } catch {
      case e: IOException => files.foreach(f => f.delete()); gnuplotFile.delete(); pngFile.delete(); return null
    }
    /* Execute gnuplot and get output streams */
    val stdout = new ArrayBuffer[String]()
    val stderr = new ArrayBuffer[String]()
    "gnuplot " + gnuplotFile.getCanonicalPath() ! ProcessLogger(s => stdout.append(s), s => stderr.append(s))
    gnuplotFile.delete()
    //stdout.foreach(s => Console.println("  " + s))
    if (stderr.length > 0) {
      Console.println("gnuplot failed with the following error message:")
      stderr.foreach(s => Console.println("  " + s))
      files.foreach(f => f.delete())
      pngFile.delete()
      return null
    }
    /* Remove temporary data files */
    files.foreach(f => f.delete())
    pngFile
  }
}


