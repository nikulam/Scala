import java.util.HashMap
package object pairSum {
  /**
   * Find whether there is an integer v1 in list l1 and an integer v2 in list l2
   * such that v1 + v2 == target.
   * If such a pair exists, return it [with "Some((v1,v2))"]; otherwise, return None.
   * This function is allowed to run in time O(l1.length * l2.length).
   */
  def hasPairSlow(l1: List[Int], l2: List[Int], target: Int): Option[(Int, Int)] = {
    val a = l1.sorted
    var b = l2.sorted
    for(i <- a) {
      val c = b.filter(_ == (target - i))
      if(!c.isEmpty) return Some(i, c(0))
    }
    None
  }

  /**
   * A faster version of hasPairSlow, must run in time
   * O(l1.length*log(l1.length) + l2.length*log(l2.length)).
   * Find whether there is an integer v1 in list l1 and an integer v2 in list l2
   * such that v1 + v2 == target.
   * If such a pair exists, return it [with "Some(( v1,v2))"]; otherwise, return None.
   */
  def hasPair(l1: List[Int], l2: List[Int], target: Int): Option[(Int, Int)] = {
   
    val x = l1.sorted
    val y = l2.sorted
    var answer: Option[(Int, Int)] = None
    val jukka = new HashMap[Int, Int]()
 
    for (i <- x) {
      if (jukka.containsKey(i)) {
        jukka.put(i, jukka.get(i + 1))
      } else jukka.put(i, 1)
    }
    
    for (i <- y) {
      var temporary = target - i
      if (jukka.containsKey(temporary) && jukka.get(temporary) != 0) {
        return Some(temporary, i)
      } 
    }
    
    answer
    
  }
}


