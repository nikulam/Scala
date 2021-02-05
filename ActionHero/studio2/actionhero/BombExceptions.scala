package studio2.actionhero


class CatastrophicException(whatHappened: String) extends Exception(whatHappened)


sealed abstract class SlightMishapException(text: String, part: Part) extends Exception(text)

/**
 * Thrown if some part can only be removed after the other part.
 */

case class RemoveEarlyException(part: Part) extends SlightMishapException("Yikes! that was close. Better remove this part first!", part)


/**
 * Thrown if some part cannot be removed before you have called "extinguish" on it.
 */

case class OnFireException(part: Part) extends SlightMishapException("Ooops! the part caught fire.... extinguish it before trying again.", part)


/**
 * Thrown if some part cannot be removed before you have called "freeze" on it.
 */

case class OverheatException(part: Part) extends SlightMishapException("Darn! its getting hot.... freeze it before anything else!!.", part)


/**
 * Thrown if some part cannot be removed before you have called "locate" on it.
 */

case class LostException(part: Part) extends SlightMishapException("Uhhhh! I lost it... better find it before going forward!!.", part)