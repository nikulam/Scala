import processing.core.PApplet

trait Enemy {
  
  val speed: Int
  var hp: Int
  var location: (Int, Int)
  
  def move: Unit
  
  def getHit(hitBy: Tower) = {
    this.hp -= hitBy.damage
  }
  
  def isDestroyed: Boolean = {
    if(this.hp > 0) false
    else true
  }
}