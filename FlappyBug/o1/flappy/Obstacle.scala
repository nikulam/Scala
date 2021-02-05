package o1.flappy
import o1._
import scala.util.Random
// This class is introduced in Chapter 2.4.
class Obstacle(val radius: Int) { 
  
  private var currentPos = randomLaunchPosition
  
  def touches(otokka: Bug) = {
    otokka.pos.distance(this.pos) < (radius + BugRadius)
  }
  def pos = this.currentPos
  def approach() = {
    if(isActive)this.currentPos = this.currentPos.addX(-ObstacleSpeed)
    else this.currentPos = randomLaunchPosition
  }
  def isActive = this.pos.x >= -this.radius
  private def randomLaunchPosition() = {
  val satunnainen = new Random()
  val launchX = ViewWidth + this.radius + satunnainen.nextInt(499)
  val launchY = satunnainen.nextInt(ViewHeight)
  new Pos(launchX, launchY)
}
  override def toString = "center at " + this.pos + ", radius " + this.radius
    
}
