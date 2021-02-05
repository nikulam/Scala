import processing.core._

class Ammo(val target: Enemy, var location: PVector, val damage: Int, val speed: Int) extends PApplet {
  
  var velocity = new PVector(0,0)
  var acceleration = new PVector(0,0)
    
  var hasHit = false
  
  def fly = {
    val tPos = new PVector(target.location.x, target.location.y) 
    acceleration = (tPos.sub(location)).setMag(2)
    velocity.add(acceleration)
    location.add(velocity.limit(speed))
    if(location.dist(target.location) < target.size / 2) {
      hasHit = true
    }
    
  }
  
}