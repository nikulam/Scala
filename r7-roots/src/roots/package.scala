
package object roots {
  import scala.math.pow

  /**
   * Find a zero points of a continuous function f (i.e., a point x such that f(x)=0.0)
   * when we know two points, lower and upper, such that f(lower) <= 0.0 and f(upper) >= 0.0.
   * Implement this with an algorithm using binary search.
   * Recall the limited precision of Doubles! That is, the new "middle point" can (and will)
   * at some point be equal to either to lower or upper point. You can stop the search when
   * this happens and return the better point (lower or upper limit point).
   *
   * The recommended programming style for this exercise is to use recursion
   * (please see the section "Binary Search" of Round 7 and Round 8 of the course material
   * if you are not familiar with recursion yet).
   */
  def zero(f: Double => Double, lower: Double, upper: Double): Double = {
    require(lower <= upper)
    require(f(lower) <= 0.0)
    require(f(upper) >= 0.0)
    
    val x = ((lower + upper) / 2)
    val y = f(x)
    
    if(-0.00001 < y && y < 0.00001) x
    else if(y >= 0) zero(f, lower, x)
    else zero(f, x, upper)
  }
  /**
   * Compute the n:th root of a non-negative Double x, i.e. the value v such that v to the n:th power equals to x.
   * Implement this by using the "zero" method above (hint: v^n = x if and only if ....... = 0.0)
   * but don't copy any code from there (this method can be implemented in one short line).
   */
  def nthRoot(n: Int)(x: Double): Double = {
    zero({v: Double => pow(v, n) - x}, 0, x)
  }
  
  /** Compute the square root of a non-negative Double x. */
  def squareRoot(x: Double): Double = nthRoot(2)(x)

  /** Compute the cube root of a non-negative Double x. */
  def cubeRoot(x: Double): Double = nthRoot(3)(x)
}


