package o1.flappy
import o1._
class Game {
  
  var bug = new Bug(startingPosition1)
  val obstacles = Vector[Obstacle](new Obstacle(70), new Obstacle (30), new Obstacle(20))
  def timePasses() = { 
    bug.fall()
    obstacles.foreach(_.approach())
  }
  
  def activateBug() = bug.flap(15)
  
  def isLost = {
    obstacles.exists(_.touches(this.bug)) || !this.bug.isInBounds
  }
}
