import scala.math._
import processing.core._
import controlP5._

class prosessi extends PApplet {

  var boids = Vector[boids]()
  var vision = Pi

  // Oletusarvot
  var radius = 100
  var maxForce = 1
  var maxVel = 10

  var atStartUp = true

  var coherence: Float = 0
  var seperation: Float = 0
  var alignment: Float = 0

  // alustetaan control jotta voimme käyttää sitä setup funktion ulkopuolella
  var cp5: ControlP5 = _

  override def setup() = {
    background(255)
    frameRate(30)

    /*
     *  Luodaan control, johon lisätään sliderit.
     *  Slidereiden piirto setupissa jotta niitä ei alusteta jokaisessa draw kutsussa
     */
    
    cp5 = new ControlP5(this)

    cp5.addSlider("coherence")
      .setPosition(50, 700)
      .setRange(0, 100)
      .setSize(200, 50)

    cp5.addSlider("seperation")
      .setPosition(50, 800)
      .setRange(0, 100)
      .setSize(200, 50)

    cp5.addSlider("alignment")
      .setPosition(50, 900)
      .setRange(0, 100)
      .setSize(200, 50)

    cp5.addSlider("radius")
      .setPosition(550, 900)
      .setRange(0, 400)
      .setSize(200, 50)
      .setValue(100)

    cp5.addSlider("maxForce")
      .setPosition(550, 800)
      .setRange(0, 10)
      .setSize(200, 50)
      .setValue(1)

    cp5.addSlider("maxVel")
      .setPosition(550, 700)
      .setRange(0, 50)
      .setSize(200, 50)
      .setValue(10)

  }

  // Simulaation koko, ei käyttäjän muutettavissa.
  override def settings() = {
    size(1000, 1000);

  }

  // lisää uusia lintuja jokaisella hiiren klikkauksella hiiren sijaintiin
  override def mouseClicked() = {
    boids = boids :+ new boids(mouseX, mouseY, maxForce, maxVel)

  }

  override def mouseDragged() = {

    // isMouseOver tarkistaa onko hiiri minkään sliderin päällä.
    
    if (!cp5.isMouseOver) {
      boids = boids :+ new boids(mouseX, mouseY, maxForce, maxVel)

    }
  }

  override def keyPressed() = {
    if (key == 'x' && boids.size > 0) {
      boids = boids.tail
    } else if (key == 'r') {
      boids = Vector[boids]()

      // alustetaan slidereiden arvot

      cp5.get("radius").setValue(100)
      cp5.get("maxVel").setValue(10)
      cp5.get("maxForce").setValue(1)

      cp5.get("coherence").setValue(100)
      cp5.get("seperation").setValue(100)
      cp5.get("alignment").setValue(100)

    } else if (key == 'v') {
      if (vision == Pi / 3) vision = Pi else vision = Pi / 3
    }
  }

  override def draw() = {
    /*
     * noStroke => ei piirretä reunoja
     * fill(180) => täytetään teksti värillä 180, default on valkoinen (255)
     * background(255) => taustan väri 255. Tausta "väritetään" joka draw kutsu uudestaan
     */

    if (atStartUp) {

      // aloitusnäyttö, siirtyy simulaatioon kun painaa näppäimistön nappia.

      textSize(30)
      fill(180)
      text("PRESS ANY KEY TO START THE SIMULATION\n\nAdd new boids by clicking or dragging with your mouse,\ndelete by pressing X, reset with r\nChange bird FOV between 120 and 360 deg with v.", 140, 300)

      textSize(20)

      text("FOV " + (vision * 360 / Pi).ceil.toString, 800, 20)

      text("Alignment", 300, 925)
      text("Seperation", 300, 825)
      text("Cohesion", 300, 725)

      text("Adjust the values of new boids", 550, 675)
      text("Adjust the weights of the forces", 50, 675)

      text("Max Velocity", 800, 725)
      text("Max Force", 800, 825)
      text("Radius", 800, 925)

      if (keyPressed) atStartUp = false

    } else {

      // itse simulaation piirtäminen
      background(255)

      textSize(20)

      text("Alignment", 300, 925)
      text("Seperation", 300, 825)
      text("Cohesion", 300, 725)

      text("Max Velocity", 800, 725)
      text("Max Force", 800, 825)
      text("Radius", 800, 925)

      text("FOV " + (vision * 360 / Pi).ceil.toString, 800, 20)

      // haetaan slidereiden arvot ja sijoitetaan kertoimiin

      coherence = cp5.get("coherence").getValue.toInt
      seperation = cp5.get("seperation").getValue.toInt
      alignment = cp5.get("alignment").getValue.toInt

      // Uusien boidien arvot

      radius = cp5.get("radius").getValue.toInt
      maxVel = cp5.get("maxVel").getValue.toInt
      maxForce = cp5.get("maxForce").getValue.toInt

      // muutetaan lintujen väriä painokertoimien mukaan

      stroke(coherence * 2.toFloat, seperation * 2.toFloat, alignment * 2.toFloat)

      // Lintujen liike

      separate()
      align()
      cohesion()
      edges()

      boids.foreach(_.move())

      for (boid <- boids) {
        strokeWeight(6)
        line(boid.pos.x, boid.pos.y, boid.pos.x + boid.v.x, boid.pos.y + boid.v.y)
      }

    }

  }

  def align() = {
    for (boid <- boids) {

      val inRadius = boids.filter(n => boid.pos.dist(n.pos) <= radius && boid != n && PVector.angleBetween(boid.v, n.pos) < vision)
      var sumVector = new PVector(0, 0)

      if (!inRadius.isEmpty) {
        for (x <- inRadius) {
          if (x != boid) sumVector.add(x.v)
        }

        sumVector.div(inRadius.size)
        sumVector.mult((alignment / 100.0).toFloat)
        boid.a.add(sumVector)
      }
    }
  }

  def cohesion() = {

    for (boid <- boids) {

      val inRadius = boids.filter(n => boid.pos.dist(n.pos) <= radius && boid != n && PVector.angleBetween(boid.v, n.pos) < vision)
      var sumVector = new PVector

      if (!inRadius.isEmpty) {
        for (x <- inRadius) {
          if (x != boid) sumVector.add(x.pos)
        }

        sumVector.div((1.0 * inRadius.size).toFloat)
        sumVector.sub(boid.pos)
        sumVector.setMag(maxVel)
        sumVector.sub(boid.v)
        sumVector.limit(maxForce)
        sumVector.mult((coherence / 100.0).toFloat)
        boid.a.add(sumVector)
      }
    }
  }

  def separate() = {

    for (boid <- boids) {
      val inRadius = boids.filter(n => boid.pos.dist(n.pos) <= radius && boid != n && PVector.angleBetween(boid.v, n.pos) < vision)
      var sumVector = new PVector(0, 0)

      if (!inRadius.isEmpty) {
        for (x <- inRadius) {
          val v = new PVector(boid.pos.x - x.pos.x, boid.pos.y - x.pos.y)
          val distance = boid.pos.dist(x.pos)
          v.div(distance) // pitäisi olla distance*distance mutta tällöin voimasta tulee liian heikko. div(distance) eroaa lopputulokseltaan hyvin vähän distance^2 versiosta
          sumVector.add(v)

        }

        val v = sumVector.div((1.0 * (inRadius.size)).toFloat)
        boid.a.add(v.mult((seperation / 100.0).toFloat))
      }
    }
  }

  // Pitää boidit alueella.
  def edges() {

    for (boid <- boids) {
      if (boid.pos.x > width) {
        boid.pos.x = 0;
      } else if (boid.pos.x < 0) {
        boid.pos.x = width;
      }
      if (boid.pos.y > height) {
        boid.pos.y = 0;
      } else if (boid.pos.y < 0) {
        boid.pos.y = height;
      }
    }
  }
}

object Launcher extends App {
  PApplet.main("prosessi")

}