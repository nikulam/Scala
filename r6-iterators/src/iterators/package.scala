
package object iterators {

import scala.math._
import scala.collection.mutable.Queue
 

/**
   * Returns an iterator that generates the prime numbers
   * 1,2,3,5,7,11,13,17,19,23,...
   * (Strictly speaking, 1 is not considered prime in most of the modern definitions as including it causes problems in some other definitions but let's consider it to be a prime in this exercise.)
   */
  def primesIterator: Iterator[Int] = new Iterator[Int] {
    /* You'll need some private var or val to store the internal state.
	 * Put it/them here */
    var luku = 1
    
	 

    /**
     * There is always the next prime number.
     * In reality, we should return false when the next prime would be larger than that can
     * be represented with an Int but, for the sake of focusing the exercise on the essential parts,
     * this consideration is not required for getting the points.
     */
    def hasNext = true

    /**
     * Get the next prime number.
     * The first call should return 1, the next 2, then 3, 5, 7, 11 and so on.
     *
     * These numbers must be computed by the code here, one is not allowed to pre-compute and
     * store them in an Array or similar data structure.
     */
    def isPrime(n: Int): Boolean = {
      if(n <= 3) return true
      if(n % 2 == 0 || n % 3 == 0) return false
      
      var i = 5
      while(i * i <= n) {
        if(n % i == 0 || n % (i + 2) == 0) return false
        i += 6
      }
      true 
    }
    
    
    
    def next(): Int ={
      var found = false
      while(found != true) {
        found = isPrime(luku)
        luku += 1
      }
      luku - 1
    }
  }

  /**
   * Return a new iterator whose "next" method returns randomly an element that
   * - is in the non-empty constructor argument set s, and
   * - has not been returned during the blockingTime last calls to "next".
   * That is, after being selected and returned by "next", an element is blocked in the
   * the "blockingTime" subsequent calls to "next".
   * The argument seed is the seed given to the pseudo-random number generator
   * (fix it to have repeatable runs and set to System.nanoTime etc to have true pseudo-random sequences).
   */
  def blockingRandomIterator[T](s: Set[T], blockingTime: Int, seed: Int = 2105): Iterator[T] = new Iterator[T] {
    require(0 <= blockingTime && blockingTime < s.size)
    /* Pseudo-random number generator.
     * See http://www.scala-lang.org/api/current/index.html#scala.util.Random */
    val r = new scala.util.Random(seed)
    var a = Queue[T]()
    var buf = s.toBuffer

    /* You'll need some var or val to store the internal state.
     * 
     * Perhaps a queue to keep track of the blocked elements?
     * See http://en.wikipedia.org/wiki/Queue_%28abstract_data_type%29 and
     * http://docs.scala-lang.org/overviews/collections/concrete-mutable-collection-classes.html
     * 
     * And perhaps a mutable Buffer to store the elements that are not blocked?
     * By the way, a fast method to remove an element at index i from an unordered buffer
     * is to just replace that element with the last element of the buffer and then shrink
     * the buffer by dropping the last element (which is now a copy of the one in the i:th position)
     * with trimEnd(1)
     * 
     * Put your state vars/vals here. */

    
    /** There is always the next element */
    def hasNext = true

    /** Returns a random, non-blocked element */
    def next(): T = {
    
      var index = r.nextInt(buf.size)    
      var joku = buf(index)
        
      if(a.size == blockingTime) {
        buf(index) = buf(buf.size - 1)
        buf.trimEnd(1)
        a.enqueue(joku)
        buf += a.dequeue()
        joku
      } 
      else {
        buf(index) = buf(buf.size - 1)
        buf.trimEnd(1)
        a.enqueue(joku)
        joku
      }
    }
  }
}


