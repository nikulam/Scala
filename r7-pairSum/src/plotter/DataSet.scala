
package plotter

/** A simple wrapper for two dimensional data sets */
case class DataSet[XT <% Ordered[XT], YT](val name: String, val points: Seq[Tuple2[XT, YT]]) {

}


