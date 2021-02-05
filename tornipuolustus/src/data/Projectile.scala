package data

import o1.{Pos, Direction}
import scala.collection.mutable.Buffer

trait Projectile {
  
  val projectileType: String  //used to tell processing what to draw ie. for each "dart" processing draws a black dot
  val origin: Tower
  var lifetime: Int = 0       //current lifetime of this
  var health: Int = origin.pierce
  var pos: Pos = origin.pos
  val speed: Int
  val maxLifetime: Int //= (this.origin.range.toDouble / this.speed).toInt  //for some reason there are errors if the maxLifetime is calculated here
  val target: Pos = origin.pos
  val direction: Direction = this.pos.directionOf(this.target)
  val hitbox: Int = 10       //how far the projectile can be from the enemy and still hit it
  var damage = origin.damage //how many layers this projectile destroys
  var prevEnemies: Buffer[Enemy] = Buffer() //the same projectile cannot hit the same enemy multiple times
  
  
//  //ver. 1: only takes one enemy to hit per frame. this can cause piercing projectiles to go through enemies without hitting them.
//  def hit() { 
////    println("hit commenced")
//    if(!origin.level.enemyBuffer.isEmpty) {
//      val hitEnemy = origin.level.enemyBuffer.minBy( e => this.pos.distance(e.pos))
////      println("enemyfound")
//      if (this.pos.distance(hitEnemy.pos) <= hitbox + hitEnemy.scale/2 && !prevEnemies.contains(hitEnemy)) {
////        println("enemyhit")
//        this.health -= 1
//        if (this.health <= 0)origin.projectiles -= this  
//        hitEnemy.hit(damage)
//        prevEnemies += hitEnemy
////        println("enemydamaged")
//      }
//    }
////    println("hit done")
////    println("projectilebuffer: " + origin.projectiles)
//  }
  
  
  //ver. 2: can take multiple enemies to hit per frame. makes piercing projectiles able to actually work
  def hit() { 
    if(!origin.level.enemyBuffer.isEmpty) {
      val hitEnemies = origin.level.enemyBuffer.sortBy( e => this.pos.distance(e.pos) ).take(this.health)
      for (hitEnemy <- hitEnemies) {
        if (this.pos.distance(hitEnemy.pos) <= (hitbox + hitEnemy.scale.toDouble/2) && !prevEnemies.contains(hitEnemy)) {
          this.health -= 1
          if (this.health <= 0)origin.projectiles -= this  
          hitEnemy.hit(damage)
          prevEnemies += hitEnemy
        }
      }
    }
  }
  
  
  def move() {  //move the projectile each tick and if there's an enemy too close, hit it.
    lifetime += 1
    val newPos = new Pos((direction.dx*speed) +this.pos.x,    (direction.dy*speed) +this.pos.y)
    this.pos = newPos
    if (lifetime >= maxLifetime) {
      origin.projectiles -= this
    }
    if (origin.level.enemyBuffer.exists( e => e.pos.distance(this.pos) <= (hitbox + e.scale.toDouble/2) )) this.hit()
  }
}