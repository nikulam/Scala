package studio2.actionhero

/**
 * This object is where you can safely try to be a hero and defuse a bomb.
 * 
 * There are tests available in studio2.actionhero.tests.DefusingTest
 */

object ImaginaryActsOfCourage {

  /**
   * Defuses a bomb. You are to implement this method. It removes the parts of a bomb.
   */
  
  def defuse(bomb: Bomb) = {
        
    val parts = bomb.parts
    var vek = Vector[Part]()
    
    while (!bomb.defused) {
      
    
    for (osa <- parts) {
          
      try {
        osa.remove
      }
      
      catch {
        case RemoveEarlyException(_) => {
          osa.removeThisFirst
        }
        case OnFireException(_) => {
          osa.extinguish()
        }
        case OverheatException(_) => {
          osa.freeze()
        }
        case LostException(_) => {
          osa.locate()
        }
        
      
      
      }
    }
    
    }

    /**
     *               Write your code here.
     * 
     *   It should go through the parts and attempt to remove
     *   all the parts. In many cases this throws an exception,
     *   which is acted upon appropriately (look in Bomb.scala)
     *   
     *   You can attempt to remove a part once. If it throws an exception
     *   handle that before trying again.
     * 
     *   The solution is not long. The whole aim of the exercise
     *   is just to try out exceptions and matching.
     * 
     *   Some parts need to be removed before others. If you want to
     *   you get information about this with an exception. Those who want
     *   a challenge can attempt to do a topological sort and remove the parts in
     *   the correct order.
     *   
     */
    
  }
  
}