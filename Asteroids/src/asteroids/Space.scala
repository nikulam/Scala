package asteroids

import scala.collection.mutable.Buffer
import java.awt.Graphics2D

/**
 * Avaruus joka sisältää asteroideja
 */
object Space {
  
  // asteroidit säilötään tänne
  val asteroids = Buffer[Asteroid]()
  
  // Kolme esimerkkiasteroidia
  asteroids += Asteroid(100, Vector2D(0.5,0.5),   Vector2D(100,100),  0.002)
  asteroids += Asteroid(130, Vector2D(-0.45,0.3), Vector2D(200,150), -0.0012)
  asteroids += Asteroid(90,  Vector2D(0.1,0.7),   Vector2D(400,500),  0.003)
  
  // Kun avaruus astuu askeleen edemm�s, kaikki asteroiditkin liikkuvat askeleen eteenpäin
  def step() = {
    asteroids.foreach(_.move())
  }
  
  // Jos avaruuteen , koordinaatteihin x, y ammutaan, etsitään kaikki
  // asteroidit, joihin osuttiin ja korvataan ne pienemmillä, hajonneilla asteroideilla
  def shoot(x: Int, y: Int) = {
    val hitted = asteroids.filter(_.contains(x,y))
    asteroids --= hitted
    asteroids ++= hitted.flatMap(_.hit())
  }
  
  // Avaruuden piirtäminen on asteroidien piirtämistä
  def draw(g: Graphics2D) = asteroids foreach (_.draw(g))
}
