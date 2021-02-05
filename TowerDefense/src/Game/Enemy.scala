import processing.core._

trait Enemy extends PApplet {
  
  var hp: Int 
  val speed: Int 
  val value: Int
  
  val size = 50
  
  var count = 0
  var location = new PVector(-100,180)
  var reachedGoal = false
  
  var velocity = new PVector(0,0)
  var acceleration = new PVector(0,0)
  
  def move(map: Map) = {
    val r = map.route2
    if(count < r.size) {
      val p = r(count)
      val tPos = new PVector(p.x, p.y) 
      acceleration = (tPos.sub(location))
      velocity.add(acceleration)
      location.add(velocity.limit(speed))
      if(location.dist(map.goal) < 5) reachedGoal = true
      if(location.dist(p).toInt < 5) count += 1
    }
  }
  
  def getHit(hitBy: Ammo) = {
    this.hp -= hitBy.damage
  }
  
  def isDestroyed: Boolean = {
    hp <= 0
  }
  
}
//The hp of enemy shouldn't be over 255. See processingTest.enemyDisp
class BasicEnemy extends Enemy {
  val speed = 4
  var hp = 60
  val value = 35
}
class FastEnemy extends Enemy {
  val speed = 6
  var hp = 50
  val value = 50
}
class StrongEnemy extends Enemy {
  val speed = 3
  var hp = 255
  val value = 150
  
}