
package subsetFinder

class KSubsetTester[T](override val domain: scala.collection.immutable.Set[T],
  private val secretSubset: Set[T]) extends Tester[T] {
  require(domain.size >= 1, "The domain must include at least one element")
  require(secretSubset subsetOf domain, "secret subset must be a subset of the domain")
  private var _calls: Int = 0
  override def nofCalls: Int = _calls
  override def contains(s: Set[T]) = { _calls += 1; !(s intersect secretSubset).isEmpty }
}

