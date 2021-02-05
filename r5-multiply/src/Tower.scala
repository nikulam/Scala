import scala.collection.mutable.Buffer
import scala.swing._
import scala.math._

import processing.core.PApplet

trait Tower {
  
  val damage: Int
  val price: Int
  val range: Int
  var enemies: Buffer[Enemy]
  val location: (Int, Int)
  val fireRate: Int
  
  
  def hasTarget: Boolean = {this.findTarget != None}
  
  def fire = {
    this.findTarget match {
      case Some(vihu) => vihu.getHit(this)
    }
  }
  
  def distance(vihu: Enemy): Double = {  // Pythagoras
    sqrt(pow((this.location._1 - vihu.location._1), 2) + pow((this.location._2 - vihu.location._2), 2))
  }
  
  def findTarget: Option[Enemy] = {
    val inRange = this.enemies.filter(n => distance(n) <= this.range)
    if(!inRange.isEmpty) Some(inRange(0))
    else None
  }
  
}


