package asteroids

import java.awt.Graphics2D
import java.awt.geom.{GeneralPath, Path2D}

/**
 * Asteroidilla on massa (joka näkyy sen säteenä), nopeusvektori, paikkavektori, ja pyärimisnopeus
 */
case class Asteroid(mass: Double, speed: Vector2D, var place: Vector2D, rotation: Double) {
  
  // pyörimiskulma
  var angle = 0.0

  // Asteroidi on muodoltaan ympyrä, jonka pisteitä on poikkeutettu satunnaisesti
  val shape = {
    val angles  = 0.0 to (2*math.Pi) by (0.2*math.Pi)
    val radii   = List.fill(10)(mass + 0.6 * util.Random.nextDouble() * mass)

    val xCoords = for {
      (r, theta) <- (radii zip angles)
    } yield r * math.cos(theta)

    val yCoords = for {
      (r, theta) <- (radii zip angles)
    } yield r * math.sin(theta)

    val polyline = new GeneralPath(Path2D.WIND_NON_ZERO, 10)  
    polyline.moveTo(xCoords.head, yCoords.head)
    
    for ((x,y) <- (xCoords.tail zip yCoords.tail))
      polyline.lineTo(x,y)
      
    polyline
  }
  
  // Osuma asteroidiin tuottaa listan pienempiä asteroideja jotka lähtevät osin vastakkaisiin suuntiin
  def hit(): List[Asteroid] = List(
    Asteroid(mass / 1.7, speed.derive( 0.5,1.1), place, rotation * -1.3),  
    Asteroid(mass / 1.8, speed.derive(-0.5,1.1), place, rotation * 1.3)   
  )
  
  // Asteroidi liikkuu eteenpäin lisäämällä nopeusvektorin paikkavektoriin.
  // paikkavektori "pyörähtää ympäri" jos asteroidi on karkaamassa liian kauas näytettävältä
  //ruudulta
  def move() = {
    place = place + speed
    place = place.bound(AsteroidGame.width, (4 * mass).toInt, AsteroidGame.height, (4 * mass).toInt)
    angle += rotation // asteroidi myäs pyörii  
  }
  
  // osuminen testataan siirtämällä hiiren osumapiste asteroidin paikan mukaisesti origoon ja
  // kääntämällä vektoria asteroidin kulman mukaisesti
  def contains(x: Int, y: Int) = {
    val x2 = x-place.x
    val y2 = y-place.y
    
    val theta = math.atan2(y2, x2)
    val len   = math.hypot(x2, y2)
    
    val cont = shape.contains(len * math.cos(theta - angle), len * math.sin(theta - angle))

    cont
  }
  
  /**
   * Piirrettäessä asteroisi siirretään paikalleen avaruudessa, käännetään oikeaan suuntaan ja lopuksi
   * kuvio piirretään täytettynä. Tämän jälkeen koordinaatisto palautetaan ennalleen.
   */
  def draw(g: Graphics2D) = {
    val oldTransform = g.getTransform()
    
    g.translate(place.x, place.y)
    g.rotate(angle)
    g.fill(shape)
    
    g.setTransform(oldTransform)
  }
  
}