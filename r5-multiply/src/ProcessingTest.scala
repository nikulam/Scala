import processing.core._



class Processing extends PApplet {
  
  
  var location = new PVector(100,100)
  var velocity = new PVector(0,0)
  var acceleration = new PVector(0,0)
  
  val jukka = new PVector(400,400)
  
  var dLoc = new PVector(600,0)
  
  def decoy = {
    fill(255,0,0)
    circle(dLoc.x, dLoc.y, 50)
    dLoc.add(new PVector(0, 2))
  }
  
  def pursuit = {
    acceleration = new PVector(dLoc.x, dLoc.y).sub(location)
    velocity.add(acceleration.setMag(0.5.toFloat))
    location.add(velocity.limit(5))
    fill(0,255,0)
    circle(location.x, location.y, 25)
  }
  
// Pakolliset metodit
  
  override def setup() {
    background(0)
    
  }
  
  override def settings() {
    size(800,800)
  
  }
  override def draw() {
    background(0)
    decoy
    pursuit
  }
  
}

object ProcessingTest extends App {
  PApplet.main("Processing")
}
  
