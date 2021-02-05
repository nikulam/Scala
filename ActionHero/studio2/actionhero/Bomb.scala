package studio2.actionhero

/**
 * A bomb is mainly a container for it's parts. These are defined by the classess implementing this trait.
 */

trait Bomb {

  /**
   * These are the bomb parts. You should go through these and do the following:
   *
   * 1) try to remove the part. If you get any of the SlightMishapExceptions, you should
   *    act accordingly. The problem is identified by the exception type.
   *
   *    a) If the part is on fire (OnFireException)- extinguish it
   *    b) If the part is overheating (OverheatException)- freeze it
   *    c) if the part is lost(LostException)- locate it
   *    d) if the part is removed too early (RemoveEarlyException)- remove the other part first
   *
   *   Not that the parts do not get removed from the bomb - they just are marked as removed.
   */
  val parts: Vector[Part]

  /**
   * A bomb is defused when all its parts are removed and none of the parts has problems
   */

  def defused = parts.forall(_.ok)

}


/**
 * These are the three different kinds of problems a part can be scripted to have. Each problem is represented by an object
 * and having a sealed trait makes sure that there cannot be any other ones.
 */

sealed trait Problems

case object Fire extends Problems
case object Heat extends Problems
case object Lost extends Problems

// NOTE: Try adding a new object and see the compiler warn about the match-clauses not being exhaustive.



/**
 * This is a part in an imaginary Bomb you dismantle (while actually sleeping).
 *
 * Each Part has a name, information about what part should be removed first, whether it is impossible to remove the part and finally a problem that apppears only once.
 * If a part is on fire, it should be extinguished, if it is overheating, is should be frozen and if it is lost, it should be located.
 */

class Part(val name:String, val removeThisFirst: Option[Part], boobytrapped: Boolean, futureProblem: Option[Problems] = None) {

  // The state of the part is stored in these four variables:
  private var visit = false               // Whether removal has been tried, whether it has been successful
  private var successfullyRemoved = false // Whether the part has been removed
  private var problemHandled = false      // Whether the scripted problem has been handled
  private var currentProblem: Option[Problems] = None // if there is a problem currently active

  /**
   * Is this part correctly handled
   */
  def ok = successfullyRemoved && !hasProblem

  /**
   * Has removal been attempted
   */
  def accessed = visit

  /**
   * Is there a currently active problem
   */
  def hasProblem = !currentProblem.isEmpty


  /**
   * Extinguishes the part. This method should be called if the item is on fire
   */

  def extinguish() = handle(Fire)

  /**
   * Freezes the part. This method should be called if the item is overheating
   */

  def freeze()     = handle(Heat)

  /**
   * Finds a lost part. This method should be called if the item has been lost
   */

  def locate()     = handle(Lost)


  // This private method actually handles the cases above

  private def handle(p: Problems) = {
    if (currentProblem.contains(p)) {
      currentProblem = None
      problemHandled = true
    } else {
        throw new CatastrophicException("You did something useless and lost that critical second!!")
    }
  }


  /**
   * This method attempts the removal of a part. If it fails - this can lead to a number of different exceptions.
   *
   * @throws CatastrophicException if the part is boobytrapped or if you don't act on an existing problem properly
   * @throws OnFireException   if the part caught fire - you really should extinguish it
   * @throws OverheatException if the part is overheating - you really should freeze it
   * @throws LostException     if the part got lost - you really should locate it
   */

  def remove() = {

    // mark this part visited

    visit = true

    // boobytrapped parts always throw an exception and it's IMPOSSIBLE to prepare for them

    if (boobytrapped)
      throw new CatastrophicException("Noooo!!! You wake up realizing you just dismantled your mobile phone while asleep.")

    // The strange "if" inside the for-statement is a effectively a filter

    for (otherPart <- this.removeThisFirst if !otherPart.successfullyRemoved)
      throw new RemoveEarlyException(otherPart)

    // Existing problems should have been taken care about before attempting removal again

    currentProblem match {
      case Some(Fire) => throw new CatastrophicException("You burned yourself!")
      case Some(Heat) => throw new CatastrophicException("The part failed due to overheating!")
      case Some(Lost) => throw new CatastrophicException("The part you lost triggered the bomb!")
      case _ =>
    }

    // If there should be a problem, (and it wasn't yet there) then create it.

    if (!problemHandled) {
      futureProblem match {
        case Some(Fire) => this.fire()
        case Some(Heat) => this.overheat()
        case Some(Lost) => this.lose()
        case None =>
      }
    }

    // Mark the part removed - note that this is only accessed if exceptions are not thrown in the preceding code.

    successfullyRemoved = true
  }

    /**
   * Sets the item on fire
   */

  private def fire() = {
    currentProblem = Some(Fire)
    throw new OnFireException(this)
  }

  /**
   * Overheats the item
   */


  private def overheat() = {
    currentProblem = Some(Heat)
    throw new OverheatException(this)
  }

  /**
   * Loses the item.
   */

  private def lose() = {
    currentProblem = Some(Lost)
    throw new LostException(this)
  }


}
