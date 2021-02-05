
package subsetFinder

/**
 * Abstract base class for testers.
 */
abstract class Tester[T] {
  /**
   * The domain containing all the elements under consideration.
   * Some of the elements can be "important".
   */
  def domain: scala.collection.immutable.Set[T]

  /**
   * Returns true if s contains at least one "important" element.
   * The set s must be a subset of the domain.
   */
  def contains(s: Set[T]): Boolean

  /**
   * Returns the number of calls made to the "contains" method so far.
   */
  def nofCalls: Int
}

