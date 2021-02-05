package data

import scala.collection.mutable.Buffer
import scala.math._
import o1.Pos

trait Tower {
  val name: String

  var damage: Int   //damage dealt by single projectile
  var pierce: Int   //amount of different targets the projectile can hit

  val rof: Double //rate of fire (delay between shots in seconds) (a second = 60 ticks by design. you can reduce or add framerate and the towers rof should stay constant.)
  var cd = rof * game.Logic.frameRate  //game's ticks count the cooldown period up until max cd is reached, which is when the tower shoots.

  var pos: Pos = new Pos(0,0)

  val desc: String
  
  var range: Double  //the max distance between the target's pos and the tower's pos 

  val level: Level = game.Logic.currentLevel  //towers know which level they are in
  
  val price: Int 
  
  var projectiles: Buffer[Projectile] = Buffer() //the projectiles this tower has active at the same time
  
  var placed: Boolean = false  //deny shooting with a hovering tower
  def targetsInRange: Buffer[Enemy] = if (placed) this.level.enemyBuffer.filter(e => this.pos.distance(e.pos) <= this.range) else Buffer()
  def active = !targetsInRange.isEmpty && placed && cd >= rof * 60 //designed to play on 60 frames a second. changing the fps shouldnt change the relative rate of fire of towers
  
  def readyCheck { //this is used every tick
    if (active) {
      fire()
      cd = 0
    } else cd += 1
  }
  
  def fire(): Unit   //each tower has their own way of firing
  
  /*the check if the position is valid.
   * 1:if the player can afford the tower
   * 2:if there is already a tower too close
   * 3:if placement is too close to a map's waypoint.
   * 4:if placement is on the path. this uses law of cosines to calculate the angle between path line AB and the hovered point.
   *   calculating just the distance between the hovered point and path line AB is not enough as it blocks the placement even after the line ends.
   */
  def placeable(where: Pos) = game.Logic.playerMoney >= this.price && !this.level.towerBuffer.exists( t => t.pos.distance(where) < 20 ) && !this.level.waypoints.exists(wp => where.distance(wp) <= 25) && {
    !this.level.pathLineEqs.exists( pl => 
      (acos( ( pow(pl._4.distance(pl._5),2) + pow(pl._4.distance(where),2) - pow(pl._5.distance(where),2) )/( 2 * pl._4.distance(pl._5) * pl._4.distance(pl._5) ) ) <= Pi/2 ||
      acos( ( pow(pl._4.distance(pl._5),2) + pow(pl._4.distance(where),2) - pow(pl._5.distance(where),2) )/( 2 * pl._4.distance(pl._5) * pl._4.distance(pl._5) ) ) >= 3*Pi/2) &&
      (abs(pl._1*where.x + pl._2*where.y + pl._3)/sqrt(pl._1*pl._1 + pl._2*pl._2) <= 25)
    )  
  }
  
  
  def place(where: Pos) { //the method that is called to place down the tower
    if (placeable(where) ) {
      println("placed")
      placed = true
      this.pos = where
      this.level.towerBuffer += this
      game.Logic.playerMoney -= this.price
    }
  }
}