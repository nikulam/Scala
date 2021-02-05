package armlet
import java.io.{File,FileReader,BufferedReader,IOException}

/**
 * Construct a Ticker object from a user-supplied file.
 */
object launchTicker {
  def main(args: Array[String] = Array()): Unit = {
    try { // the file may not exist
      val reader = new BufferedReader(new FileReader(new File(args(0))))
      val lines = new StringBuilder()
      var line = reader.readLine()
      while (line != null) {
        lines ++= line + "\n"
        line = reader.readLine()
      }
      reader.close()
      new Ticker(lines.mkString, args(0))
    }
    catch {
      case noFile: ArrayIndexOutOfBoundsException => new Ticker()
      case invalidFile: IOException => new Ticker(filename = args(0))
    }
  }
}
