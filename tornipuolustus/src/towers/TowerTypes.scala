package towers
import o1.Pos 
import o1.Direction
import scala.math._

import data.Tower
import data.Level
import data.Enemy
import data.Projectile

class Basic extends Tower{
  val name = "Basic tower"
  var damage = 1       //damage dealt by single projectile
  var pierce = 1       //amount of different targets the projectile can hit
  val rof = 0.01  
  val desc = "Shoots out a single projectile at the enemy."
  var range = 150.0  //radius of tower range  150.0
  val price = 120
  def fire() = {  //TODO: change this to actually shoot out a projectile that deals the damage.
    
    val target = targetsInRange.maxBy(_.progress).pos   //hit an enemy that has progressed the most. 
    val a = new TargetDart(this, target)
    this.projectiles += a
    
  }
}

class TackShooter extends Tower {
  val name = "Tack shooter"
  var damage = 1
  var pierce = 1
  val rof = 0.8
  val desc = "Shoots tacks in 8 directions."
  var range = 100.0
  val price = 150
  var directions = 8        //how many directions this tower will shoot. variable just in case i decide to implement tower upgrading

  
  def fire() = {
    
    for (d <- 1 to directions) {
      val dir = Direction.fromRadians(((d-1)*(360.0/directions))/360.0 * 2*Pi)    //direction of this projectile in radians
      val a = new DirectionDart(this, dir)
      this.projectiles += a
    }
    
  }
}

class BombTower extends Tower {
  val name = "Bomb tower"
  var damage = 1
  var pierce = 1
  val rof = 0.1
  val desc = "Fires a bomb that deals damage in an area."
  var range = 200.0
  val price = 300
  
  def fire() = {
    
    val target = targetsInRange.maxBy(_.progress).pos  
    val a = new Bomb(this, target)
    this.projectiles += a
    
  }
}