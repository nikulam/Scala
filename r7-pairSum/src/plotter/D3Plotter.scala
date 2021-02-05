
package plotter

import java.io.{ File, FileWriter }
import java.io.IOException
import scala.collection.mutable.ArrayBuffer
import scala.sys.process._

/**
 * A simple class for plotting data sets with D3.js.
 * @author Tommi Junttila
 */
class D3Plotter {
  private var _xLogScale = false
  def xLogScale = _xLogScale
  /** Set/unset the x-axis to use logarithmic scale (use "plotter.xLogScale = true" to set log-scale). */
  def xLogScale_=(v: Boolean): Unit = _xLogScale = v

  private var _yLogScale = true
  def yLogScale = _yLogScale
  /** Set/unset the y-axis to use logarithmic scale (use "plotter.yLogScale = true" to set log-scale). */
  def yLogScale_=(v: Boolean): Unit = _yLogScale = v

  private var _width = 900
  /** Get the width of the plot */
  def width = _width
  /** Set the width of the plot */
  def width_=(v: Int) { require(0 < v && v < 4096); _width = v }

  private var _height = 500
  /** Get the height of the plot */
  def height = _height
  /** Set the height of the plot */
  def height_=(v: Int) { require(0 < v && v < 3072); _height = v }

  private var _leftMargin = 50
  /** Get the left margin of the plot */
  def leftMargin = _leftMargin
  /** Set the left margin of the plot */
  def leftMargin_=(v: Int) { require(0 < v && v < 1000); _leftMargin = v }

  private var _rightMargin = 100
  /** Get the right margin of the plot */
  def rightMargin = _rightMargin
  /** Set the right margin of the plot */
  def rightMargin_=(v: Int) { require(0 < v && v < 1000); _rightMargin = v }

  private var _yrange: Option[Tuple2[Double, Double]] = None
  def yrange = _yrange
  def yrange_=(p: Tuple2[Double, Double]) = { require(p._1 < p._2); _yrange = Some(p) }
  def yrange_=(s: String) = { require(s == "auto"); _yrange = None }

  /**
   * Plot the argument data sets with D3.js.
   * The resulting HML file "plot.html" is produced in the "htmlPlot"-folder of this project.
   */
  def plot[XT <% Ordered[XT], YT](xAxisName: String, yAxisName: String, dataSets: DataSet[XT, YT]*): Unit = {
    require(dataSets.length > 0, "Must provide at least one data set")
    /* Write data sets in htmlPlot/data.csv */
    val dataFile = new File(new File("htmlPlot"), "data.csv")
    try {
      var ostream: FileWriter = null
      try {
        ostream = new FileWriter(dataFile)
        // Write header line
        ostream.write("n, " + (dataSets.map(ds => ds.name).mkString(", ")) + "\n")
        val dataMaps = dataSets.map(ds => ds.points.toMap)
        val nValues = dataMaps.foldLeft(Set[XT]())((result, m) => result union m.keySet)
        for (n <- nValues.toSeq.sorted) {
          ostream.write(n + ", " + dataMaps.map(m => if (m.contains(n)) m(n).toString else "").mkString(", ") + "\n")
        }
      } finally {
        if (ostream != null) ostream.close()
      }
    } catch {
      case e: IOException => throw e
    }
    /* Make the html file containing javascript code */
    val f = new File(new File("htmlPlot"), "plot.html")
    try {
      var ostream: FileWriter = null
      try {
        ostream = new FileWriter(f)
        ostream.write(htmlPreamble)
        ostream.write("""
<svg class="linesPlot" id="ex1plot"></svg>
<script>window.onresize = makeLinesPlot("ex1plot", "data.csv", """ +
          (if (xLogScale) "\"log\"" else "\"linear\"") + """, """ +
          (if (yLogScale) "\"log\"" else "\"linear\"") + """, """ +
          width + """, """ + height + """, """ + leftMargin + """, """ + rightMargin + """);
</script>
""")
        ostream.write(htmlPostamble)
        ostream.close()
      } finally {
        if (ostream != null) ostream.close()
      }
    } catch {
      case e: IOException => throw e
    }
  }

  val htmlPreamble = """
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title> A plot</title>
  <script src="http://d3js.org/d3.v3.js"></script>
</head>
<body>
<style>
svg.linesPlot {
    font: 12px sans-serif;
    border: 1px solid #ccd;
    border-radius: 5px;
    background: #eef;
}
svg.linesPlot .axis path,
svg.linesPlot .axis line {
    fill: none;
    stroke: #000;
    shape-rendering: crispEdges;
}
svg.linesPlot .x.axis path {
    #display: none;
    stroke: #000;
}
svg.linesPlot .line {
    fill: none;
    stroke: steelblue;
    stroke-width: 1.5px;
}
</style>
</head>
<body>
<script>
function makeLinesPlot(htmlElement, dataFile, xscale, yscale, width, height, left, right) {
    var margin = {top: 20, bottom: 30, left: left, right: right};
    var plotWidth = width - margin.left - margin.right;
    var plotHeight = height - margin.top - margin.bottom;

    var x = (xscale == "log")?
    d3.scale.log().range([0, plotWidth]):
    d3.scale.linear().range([0, plotWidth]);

    var y = (yscale == "log")?
	d3.scale.log().range([plotHeight, 0]):
	d3.scale.linear().range([plotHeight, 0]);

    var color = d3.scale.category10();
    
    var xAxis = d3.svg.axis().scale(x).orient("bottom");

    var yAxis = d3.svg.axis().scale(y).orient("left");

    var line = d3.svg.line()
	//.interpolate("basis")
	.x(function(d) { return x(d.n); })
	.y(function(d) { return y(d.runtime); });

    var svg = d3.select("#"+htmlElement) //.append("svg")
	//.attr("viewBox", "0 0 " + width + " " + height )
        //.attr("preserveAspectRatio", "xMidYMid meet")
	.attr("width", width)
	.attr("height", height)
	.append("g")
	.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    d3.csv(dataFile, function(error, data) {
	color.domain(d3.keys(data[0]).filter(function(key) { return key !== "n"; }));
	
	var algs = color.domain().map(function(name) {
	    return {
		name: name,
		values: data.map(function(d) {
		    return {n: d.n, runtime: +d[name]};
		})
	    };
	});
	
	x.domain([
	    d3.min(data, function(d) { return parseFloat(d.n); }),
	    d3.max(data, function(d) { return parseFloat(d.n); })
	]);

	y.domain([
	    d3.min(algs, function(c) { return d3.min(c.values, function(v) { return v.runtime; }); }),
	    d3.max(algs, function(c) { return d3.max(c.values, function(v) { return v.runtime; }); })
	]);
	
	svg.append("g")
	    .attr("class", "x axis")
	    .attr("transform", "translate(0," + plotHeight + ")")
	    .call(xAxis)
	    .append("text")
	    .attr("x", plotWidth)
	    .attr("dy", "-.71em")
	    .style("text-anchor", "start")
	    .text("n");
	
	svg.append("g")
	    .attr("class", "y axis")
	    .call(yAxis)
	    .append("text")
	    .attr("transform", "rotate(-90)")
	    .attr("y", 6)
	    .attr("dy", ".71em")
	    .style("text-anchor", "end")
	    .text("run time (seconds)");
	
	var alg = svg.selectAll(".alg")
	    .data(algs)
	    .enter().append("g")
	    .attr("class", "alg");
	
	alg.append("path")
	    .attr("class", "line")
	    .attr("d", function(d) { return line(d.values); })
	    .style("stroke", function(d) { return color(d.name); });
	
	alg.append("text")
	    .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
	    .attr("transform", function(d) { return "translate(" + x(d.value.n) + "," + y(d.value.runtime) + ")"; })
	    .attr("x", 3)
	    .attr("dy", ".35em")
	    .text(function(d) { return d.name; });
    });
};
</script>
<h1>The latest plot</h1>
<p><b>Remember to reload this page</b> after running <tt>pairSum/plotTest.scala</tt></p>
"""
  val htmlPostamble = """
</body>
</html>
"""
}


