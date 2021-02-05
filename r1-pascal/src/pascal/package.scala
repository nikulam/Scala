
package object pascal {
  /**
   * Returns the k:th value in the n:th row of the Pascals's triangle
   * (that is, the binomial coefficient (n k), or the number of how many ways
   *  we can select k items from a set of n items).
   * For more information, see e.g.
   * - http://en.wikipedia.org/wiki/Pascal%27s_triangle
   * - http://en.wikipedia.org/wiki/Binomial_coefficient
   *
   * As this is a basic exercise on recursion,
   * you are not allowed to use var-declarations, Arrays or
   * any scala.collection data types.
   */
  def getCoefficient(n: Int, k: Int): Int = {
    require(n >= 0)
    require(0 <= k && k <= n)
    if(k == n) 1
    else if(k == 0) 1
    else {
      getCoefficient(n - 1, k - 1) + getCoefficient(n - 1, k)
    }
  }
}


