import processing.core._
import scala.math._

class boids(sx: Float, sy: Float, maxForce: Float, maxVel: Float) {

  // Kiihtyvyys. Rajoitettu arvolla maxForce

  var a =  new PVector(0, 0)
  
  // Nopeus. Rajoitettu Arvolla maxVel

  var v = new PVector(10, 0).setMag(maxVel)

  // Sijainti
  
  var pos = new PVector(sx, sy)
  
  def move() = {
    
    a.limit(maxForce)
    v.add(a)
    a.mult(0)
    v.limit(maxVel)
    pos.add(v)
    
  }

}