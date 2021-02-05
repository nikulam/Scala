import scala.math._
import processing.core._

trait Tower extends PApplet {
  val name: String
  val fireRate: Double //How many times per second.
  val range: Int
  val price: Int 
  var location: PVector
  val description: String
  
  var enemies = Array[Enemy]()
  
  def setEnemies(a: Array[Enemy]) {
    enemies = a
  }
  def setLocation(v: PVector) {
    location = v
  }
  
  var target: Option[Enemy] = None
  
  var hasTarget = {this.target != None}
  
  def distance(vihu: Enemy): Double = {
    new PVector(location.x + 25, location.y + 25).dist(vihu.location)
  }
  
  //Finds the target from enemies.
  def findTarget = {
    val inRange = this.enemies.filter(n => distance(n) <= this.range)
    if(!inRange.isEmpty) { 
      hasTarget = true
      target = Some(inRange(0))
    }
    else {
      target = None
      hasTarget = false
    }
  }
  
}

class BasicTower extends Tower {
  val name = "basic"
  val fireRate = 1.0
  val range = 200
  val price = 100
  var location = new PVector(0,0)
  val description = "Basic Tower"
}
class SniperTower extends Tower {
  val name = "sniper"
  val fireRate = 0.3
  val range = 400
  val price = 200
  var location = new PVector(0,0)
  val description = "Sniper Tower"
}
class GatlingTower extends Tower{
  val name = "gatling"
  val fireRate = 5
  val range = 150
  val price = 400
  var location = new PVector(0,0)
  val description = "Gatling Tower"
}


