
import scala.language.implicitConversions

package object expressions {
  /*
   * These implicit definitions allow us to write "2.0*x" instead of Num(2.0)*x,
   * where x is a Var object.
   */
  implicit def doubleToNum(v: Double) = Num(v)
  implicit def intToNum(v: Int) = Num(v)
}


