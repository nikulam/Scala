package asteroids

/**
 * Kuvaa kaksiulotteista matemaattista vektoria
 */
case class Vector2D(x: Double, y: Double) {

  val length = math.hypot(x, y)
  val angle  = math.atan2(y, x)

  /**
   * johtaa vektorista uuden vektorin, jonka suuntaa on poikkeutettu ja pituutta skaalattu
   */
  def derive(angleOff: Double, lengthTimes: Double) = {
    val newLen   = length * lengthTimes
    val newAngle = angle + angleOff 
    Vector2D(math.cos(newAngle) * newLen, math.sin(newAngle) * newLen)
  }
  
  /**
   * Lakee yhteen kaksi vektoria ja palauttaa uuden vektorin
   */
  def + (other: Vector2D) = {
    Vector2D(x + other.x, y + other.y)    
  }

  /**
   * Koordinaatit "pyörähtävät ympäri" jotta asteroidit putkahtavat uudestaan toiselta puolelta
   * bound saa parametreina ikkunan koon ja kappaleen koon.
   */
  
  def bound(xBound: Int, shapeWidth: Int, yBound: Int, shapeHeight: Int) = {
    val newX = 
      if (x >= xBound+shapeWidth) 
        x - xBound - 2* shapeWidth
      else if (x < -shapeWidth)
        x + xBound + 2* shapeWidth
      else x
      
    val newY = 
      if (y >= yBound+shapeHeight) 
        y - yBound - 2* shapeHeight
      else if (y < -shapeHeight)
        y + yBound + 2*shapeHeight
      else y
        
    if (newX != x || newY != y)
      Vector2D(newX, newY)
    else
      this
  }
}
